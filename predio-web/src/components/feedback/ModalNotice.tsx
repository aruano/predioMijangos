/**
 * ModalNotice — componente visual reutilizable para "alertas".
 * - No conoce de errorBus ni del backend; solo renderiza UI según props.
 * - Muestra un badge circular con ícono (success/info/warning/error).
 * - Botón primario con loader opcional; botón secundario (ghost) opcional.
 * Accesibilidad:
 * - role="dialog" + aria-modal, y labels para título/contenido.
 */
import React from 'react';
import { Button } from '@/components';
import { CSSTransition } from 'react-transition-group';

export type NoticeType = 'success' | 'info' | 'warning' | 'error';

export type NoticeAction = {
  label: string;
  /** Si la acción devuelve Promise, se mostrará loading hasta que resuelva. */
  onPress?: () => void | Promise<void | boolean>;
};

export type ModalNoticeProps = {
  open: boolean;
  type: NoticeType;
  title: string;
  message?: string;
  primary?: NoticeAction;   // requerido en la mayoría de casos
  secondary?: NoticeAction; // opcional
  onPrimary?: () => void;
  onClose?: () => void;     // cierra al tocar el fondo o al completar acciones
  primaryLoading?: boolean; // controlado desde fuera (Provider)
};

const ICONS: Record<NoticeType, React.ReactNode> = {
  success: (
    <svg width="40" height="40" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M20 6L9 17l-5-5" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  ),
  info: (
    <svg width="40" height="40" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <circle cx="12" cy="12" r="1.6" fill="white" />
      <path d="M12 18v-6" stroke="white" strokeWidth="3" strokeLinecap="round" />
    </svg>
  ),
  warning: (
    <svg width="60" height="60" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M12 8v5" stroke="white" strokeWidth="3" strokeLinecap="round" />
      <circle cx="12" cy="16.5" r="1.4" fill="white" />
    </svg>
  ),
  error: (
    <svg width="40" height="40" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M7 7l10 10M17 7L7 17" stroke="white" strokeWidth="3" strokeLinecap="round" />
    </svg>
  ),
};

const BADGE_BG: Record<NoticeType, string> = {
  success: 'var(--success-500)',
  info: 'var(--info-500)',
  warning: 'var(--warning-500)',
  error: 'var(--error-500)',
};

const PRIMARY_BG: Record<NoticeType, string> = {
  success: 'linear-gradient(180deg, var(--success-500), var(--success-600))',
  info: 'linear-gradient(180deg, var(--info-500), var(--info-600))',
  warning: 'linear-gradient(180deg, var(--warning-500), var(--warning-600))',
  error: 'linear-gradient(180deg, var(--error-500), var(--error-600))',
};

export default function ModalNotice(props: ModalNoticeProps) {
  const nodeRef = React.useRef<HTMLDivElement | null>(null);

  return (
    <CSSTransition
      in={props.open}
      timeout={220}
      classNames="modalfx"
      unmountOnExit
      mountOnEnter
      appear
      nodeRef={nodeRef}
    >
      <div ref={nodeRef} className="modal__overlay" role="dialog" aria-modal="true"
        aria-labelledby="notice-title" aria-describedby="notice-desc"
        onMouseDown={props.onClose}>
        <div className="modal__card" onMouseDown={(e) => e.stopPropagation()}>
          {/* badge + titulo + mensaje */}
          <div className="modal__badge" style={{ background: BADGE_BG[props.type] }}>
            {ICONS[props.type]}
          </div>
          <div style={{ textAlign: 'center', paddingTop: 16 }}>
            <h2 id="notice-title" style={{ margin: '16px 0 8px', fontWeight: 800, fontSize: 20 }}>
              {props.title}
            </h2>
            {props.message && (
              <p id="notice-desc" className="text-muted" style={{ margin: '0 12px 20px' }}>
                {props.message}
              </p>
            )}
          </div>
          <div style={{ display: 'grid', gap: 12, marginTop: 8 }}>
            {props.type === 'error' || props.type === 'success' ? (
              <Button
                style={{ background: props.type === 'error' ? PRIMARY_BG.error : PRIMARY_BG.success }}
                onClick={props.onClose}
              >
                Aceptar
              </Button>
            ) : (
              props.primary && (
                <Button style={{ background: PRIMARY_BG[props.type] }}
                  onClick={props.onPrimary}
                  loading={props.primaryLoading}>
                  {props.primary.label}
                </Button>
              )
            )}
            {props.secondary && (
              <Button variant="ghost" onClick={props.secondary.onPress}>
                {props.secondary.label}
              </Button>
            )}
          </div>
        </div>
      </div>
    </CSSTransition>
  );
}
