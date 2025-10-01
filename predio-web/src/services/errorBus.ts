/**
 * errorBus — Event Bus simple para notificaciones globales.
 * Propósito:
 *  - Desacoplar el manejo de errores/éxitos de la capa de llamadas HTTP.
 *  - Permite que un Provider (UI) escuche eventos y muestre modal/toast.
 */
type ErrorPayload = { status?: number; message?: string; details?: unknown };
type Handlers = {
  'api:error': ((p: ErrorPayload) => void)[];
  'api:success': ((message?: string) => void)[];
};

const handlers: Handlers = { 'api:error': [], 'api:success': [] };

export const errorBus = {
  on(event: keyof Handlers, cb: any) {
    handlers[event].push(cb);
    return () => {
      const i = handlers[event].indexOf(cb);
      if (i >= 0) handlers[event].splice(i, 1);
    };
  },
  emit(event: keyof Handlers, payload?: any) {
    handlers[event].forEach((cb) => cb(payload));
  }
};
