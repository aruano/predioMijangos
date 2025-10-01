/**
 * NotificationProvider — gestiona la cola/estado de notificaciones tipo modal.
 * Requisitos que cumples:
 *  - Maneja automáticamente errores HTTP (400/401 como "warning"; 403–599 como "error") usando errorBus.
 *  - Exponer API de contexto para disparar modales manuales: success/info/warning/error.
 *  - Permite configurar título, contenido y acciones (onPress) de cada botón.
 *  - El botón primario muestra loader si la acción devuelve Promise.
 *  - Cierra automáticamente si la acción resuelve sin devolver false.
 */
import React, { createContext, useCallback, useEffect, useMemo, useState } from 'react';
import { errorBus } from '@/services/errorBus';
import ModalNotice, { NoticeType, NoticeAction } from './ModalNotice';

type ShowArgs = {
  type: NoticeType;
  title: string;
  message?: string;
  primary?: NoticeAction;
  secondary?: NoticeAction;
  dismissible?: boolean;
};

type NoticeAPI = {
  show: (args: ShowArgs) => void;
  close: () => void;
  success: (title: string, message?: string, primary?: NoticeAction, secondary?: NoticeAction) => void;
  info: (title: string, message?: string, primary?: NoticeAction, secondary?: NoticeAction) => void;
  warning: (title: string, message?: string, primary?: NoticeAction, secondary?: NoticeAction) => void;
  error: (title: string, message?: string, primary?: NoticeAction, secondary?: NoticeAction) => void;
};

export const NotificationContext = createContext<NoticeAPI | null>(null);

export function NotificationProvider({ children }: { children: React.ReactNode }) {
  const [open, setOpen] = useState(false);
  const [type, setType] = useState<NoticeType>('info');
  const [title, setTitle] = useState('');
  const [message, setMessage] = useState<string | undefined>(undefined);
  const [primary, setPrimary] = useState<NoticeAction | undefined>(undefined);
  const [secondary, setSecondary] = useState<NoticeAction | undefined>(undefined);
  const [primaryLoading, setPrimaryLoading] = useState(false);

  const close = useCallback(() => {
    setOpen(false);
    setPrimaryLoading(false);
    // Limpia acciones para evitar fugas
    setPrimary(undefined); setSecondary(undefined);
  }, []);

  const runPrimary = useCallback(async () => {
    const fn = primary?.onPress;
    if (!fn) { close(); return; }
    try {
      const res = fn();
      if (res && typeof (res as Promise<any>).then === 'function') {
        setPrimaryLoading(true);
        const keep = await (res as Promise<void | boolean>);
        setPrimaryLoading(false);
        if (keep === false) return; // permite mantener abierto
      }
      close();
    } catch {
      setPrimaryLoading(false);
      // Mantén abierto en caso de error para que el usuario pueda reintentar
    }
  }, [primary, close]);

  const show = useCallback((args: ShowArgs) => {
    setType(args.type);
    setTitle(args.title);
    setMessage(args.message);
    setPrimary(args.primary);
    setSecondary(args.secondary ? { ...args.secondary, onPress: args.secondary.onPress ?? close } : undefined);
    setOpen(true);
  }, [runPrimary]);

  // Atajos semánticos
  const success = useCallback((t: string, m?: string, p?: NoticeAction, s?: NoticeAction) =>
    show({ type: 'success', title: t, message: m, primary: p, secondary: s }), [show]);
  const info = useCallback((t: string, m?: string, p?: NoticeAction, s?: NoticeAction) =>
    show({ type: 'info', title: t, message: m, primary: p, secondary: s }), [show]);
  const warning = useCallback((t: string, m?: string, p?: NoticeAction, s?: NoticeAction) =>
    show({ type: 'warning', title: t, message: m, primary: p, secondary: s }), [show]);
  const error = useCallback((t: string, m?: string, p?: NoticeAction, s?: NoticeAction) =>
    show({ type: 'error', title: t ?? 'Error', message: m, primary: p, secondary: s }), [show]);

  // Suscripción automática SOLO a errores (tu requisito)
  useEffect(() => {
    const off = errorBus.on('api:error', (payload: { status?: number; message?: string }) => {
      const status = payload?.status ?? 0;
      const msg = payload?.message || 'Ocurrió un problema.';
      if (status === 201) {
        warning('¡Advertencia!', msg, { label: 'Aceptar' });
      } else {
        error('¡Ha ocurrido un error!', msg, { label: 'Aceptar' });
      }
    });
    return () => off();
  }, [warning, error]);

  const value = useMemo<NoticeAPI>(() => ({
    show, close, success, info, warning, error
  }), [show, close, success, info, warning, error]);

  return (
    <NotificationContext.Provider value={value}>
      {children}
      <ModalNotice
        open={open}
        type={type}
        title={title}
        message={message}
        primary={primary}
        secondary={secondary}
        primaryLoading={primaryLoading}
        onPrimary={runPrimary}
        onClose={close}
      />
    </NotificationContext.Provider>
  );
}

export function useNotice() {
  const ctx = React.useContext(NotificationContext);
  if (!ctx) throw new Error('useNotice debe usarse dentro de <NotificationProvider>');
  return ctx;
}
