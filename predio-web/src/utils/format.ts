/**
 * @file format.ts
 * @description Utilidades de formateo para presentación de datos
 * 
 * Responsabilidades:
 * - Formateo de fechas y horas
 * - Formateo de moneda (GTQ Quetzales guatemaltecos)
 * - Formateo de teléfonos guatemaltecos
 * - Formateo de números, porcentajes y decimales
 * - Formateo de DPI y NIT guatemaltecos
 * - Normalización de strings
 * 
 * Convenciones:
 * - Fechas: DD/MM/YYYY (formato guatemalteco)
 * - Moneda: Q###,###.## (Quetzales)
 * - Teléfonos: #### #### (8 dígitos) o +502 #### #### (con código país)
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 */

// ============================================
// FORMATEO DE FECHAS
// ============================================

/**
 * Formatea una fecha al formato guatemalteco DD/MM/YYYY
 * 
 * @param date - Fecha a formatear (Date, string ISO, o timestamp)
 * @param includeTime - Si true, incluye la hora (DD/MM/YYYY HH:mm)
 * @returns Fecha formateada o string vacío si es inválida
 * 
 * @example
 * ```typescript
 * formatDate('2024-03-15'); // "15/03/2024"
 * formatDate(new Date()); // "15/03/2024"
 * formatDate('2024-03-15T10:30:00', true); // "15/03/2024 10:30"
 * ```
 */
export function formatDate(
  date: Date | string | number | null | undefined,
  includeTime = false
): string {
  if (!date) return '';

  try {
    const dateObj = date instanceof Date ? date : new Date(date);
    
    // Verificar que la fecha sea válida
    if (isNaN(dateObj.getTime())) {
      return '';
    }

    const day = String(dateObj.getDate()).padStart(2, '0');
    const month = String(dateObj.getMonth() + 1).padStart(2, '0');
    const year = dateObj.getFullYear();

    let formatted = `${day}/${month}/${year}`;

    if (includeTime) {
      const hours = String(dateObj.getHours()).padStart(2, '0');
      const minutes = String(dateObj.getMinutes()).padStart(2, '0');
      formatted += ` ${hours}:${minutes}`;
    }

    return formatted;
  } catch {
    return '';
  }
}

/**
 * Formatea una fecha al formato ISO (YYYY-MM-DD)
 * Útil para enviar fechas al backend
 * 
 * @param date - Fecha a formatear
 * @returns Fecha en formato ISO o string vacío si es inválida
 * 
 * @example
 * ```typescript
 * formatDateISO(new Date()); // "2024-03-15"
 * formatDateISO('15/03/2024'); // "2024-03-15"
 * ```
 */
export function formatDateISO(date: Date | string | number | null | undefined): string {
  if (!date) return '';

  try {
    const dateObj = date instanceof Date ? date : new Date(date);
    
    if (isNaN(dateObj.getTime())) {
      return '';
    }

    return dateObj.toISOString().split('T')[0];
  } catch {
    return '';
  }
}

/**
 * Formatea una fecha de forma relativa (hace 2 días, hace 3 horas, etc.)
 * 
 * @param date - Fecha a formatear
 * @returns String con formato relativo
 * 
 * @example
 * ```typescript
 * formatDateRelative(new Date(Date.now() - 3600000)); // "hace 1 hora"
 * formatDateRelative(new Date(Date.now() - 86400000 * 2)); // "hace 2 días"
 * ```
 */
export function formatDateRelative(date: Date | string | number | null | undefined): string {
  if (!date) return '';

  try {
    const dateObj = date instanceof Date ? date : new Date(date);
    
    if (isNaN(dateObj.getTime())) {
      return '';
    }

    const now = new Date();
    const diffMs = now.getTime() - dateObj.getTime();
    const diffSec = Math.floor(diffMs / 1000);
    const diffMin = Math.floor(diffSec / 60);
    const diffHour = Math.floor(diffMin / 60);
    const diffDay = Math.floor(diffHour / 24);
    const diffMonth = Math.floor(diffDay / 30);
    const diffYear = Math.floor(diffDay / 365);

    if (diffYear > 0) {
      return diffYear === 1 ? 'hace 1 año' : `hace ${diffYear} años`;
    }
    if (diffMonth > 0) {
      return diffMonth === 1 ? 'hace 1 mes' : `hace ${diffMonth} meses`;
    }
    if (diffDay > 0) {
      return diffDay === 1 ? 'hace 1 día' : `hace ${diffDay} días`;
    }
    if (diffHour > 0) {
      return diffHour === 1 ? 'hace 1 hora' : `hace ${diffHour} horas`;
    }
    if (diffMin > 0) {
      return diffMin === 1 ? 'hace 1 minuto' : `hace ${diffMin} minutos`;
    }
    
    return 'hace unos segundos';
  } catch {
    return '';
  }
}

/**
 * Formatea solo la hora en formato 24h (HH:mm)
 * 
 * @param date - Fecha/hora a formatear
 * @returns Hora formateada o string vacío si es inválida
 * 
 * @example
 * ```typescript
 * formatTime('2024-03-15T14:30:00'); // "14:30"
 * formatTime(new Date()); // "14:30"
 * ```
 */
export function formatTime(date: Date | string | number | null | undefined): string {
  if (!date) return '';

  try {
    const dateObj = date instanceof Date ? date : new Date(date);
    
    if (isNaN(dateObj.getTime())) {
      return '';
    }

    const hours = String(dateObj.getHours()).padStart(2, '0');
    const minutes = String(dateObj.getMinutes()).padStart(2, '0');

    return `${hours}:${minutes}`;
  } catch {
    return '';
  }
}

// ============================================
// FORMATEO DE MONEDA
// ============================================

/**
 * Formatea un número como moneda guatemalteca (Quetzales)
 * 
 * @param amount - Cantidad a formatear
 * @param includeSymbol - Si true, incluye el símbolo Q
 * @param decimals - Cantidad de decimales (default: 2)
 * @returns String formateado como moneda
 * 
 * @example
 * ```typescript
 * formatCurrency(1500); // "Q 1,500.00"
 * formatCurrency(1500.5, false); // "1,500.50"
 * formatCurrency(1500, true, 0); // "Q 1,500"
 * ```
 */
export function formatCurrency(
  amount: number | string | null | undefined,
  includeSymbol = true,
  decimals = 2
): string {
  if (amount === null || amount === undefined || amount === '') {
    return includeSymbol ? 'Q 0.00' : '0.00';
  }

  try {
    const numAmount = typeof amount === 'string' ? parseFloat(amount) : amount;
    
    if (isNaN(numAmount)) {
      return includeSymbol ? 'Q 0.00' : '0.00';
    }

    const formatted = numAmount.toLocaleString('es-GT', {
      minimumFractionDigits: decimals,
      maximumFractionDigits: decimals,
    });

    return includeSymbol ? `Q ${formatted}` : formatted;
  } catch {
    return includeSymbol ? 'Q 0.00' : '0.00';
  }
}

/**
 * Parsea un string de moneda a número
 * Elimina símbolos, espacios y comas
 * 
 * @param currencyString - String de moneda a parsear
 * @returns Número parseado
 * 
 * @example
 * ```typescript
 * parseCurrency('Q 1,500.50'); // 1500.5
 * parseCurrency('1,500.50'); // 1500.5
 * ```
 */
export function parseCurrency(currencyString: string | null | undefined): number {
  if (!currencyString) return 0;

  try {
    // Eliminar símbolos de moneda, espacios y comas
    const cleaned = currencyString
      .replace(/Q/g, '')
      .replace(/\s/g, '')
      .replace(/,/g, '');

    const parsed = parseFloat(cleaned);
    return isNaN(parsed) ? 0 : parsed;
  } catch {
    return 0;
  }
}

// ============================================
// FORMATEO DE TELÉFONOS
// ============================================

/**
 * Formatea un teléfono guatemalteco (8 dígitos)
 * 
 * @param phone - Número de teléfono a formatear
 * @param includeCountryCode - Si true, incluye +502
 * @returns Teléfono formateado
 * 
 * @example
 * ```typescript
 * formatPhone('12345678'); // "1234 5678"
 * formatPhone('12345678', true); // "+502 1234 5678"
 * formatPhone('50212345678'); // "1234 5678" (elimina código de país automáticamente)
 * ```
 */
export function formatPhone(
  phone: string | number | null | undefined,
  includeCountryCode = false
): string {
  if (!phone) return '';

  try {
    // Convertir a string y limpiar
    let cleaned = String(phone).replace(/\D/g, '');

    // Eliminar código de país si existe (502)
    if (cleaned.startsWith('502') && cleaned.length > 8) {
      cleaned = cleaned.substring(3);
    }

    // Validar que tenga 8 dígitos
    if (cleaned.length !== 8) {
      return String(phone); // Retornar original si no es válido
    }

    // Formatear: #### ####
    const formatted = `${cleaned.substring(0, 4)} ${cleaned.substring(4)}`;

    return includeCountryCode ? `+502 ${formatted}` : formatted;
  } catch {
    return String(phone);
  }
}

/**
 * Limpia un teléfono dejando solo números
 * Elimina código de país si existe
 * 
 * @param phone - Teléfono a limpiar
 * @returns Solo los 8 dígitos del teléfono
 * 
 * @example
 * ```typescript
 * cleanPhone('+502 1234 5678'); // "12345678"
 * cleanPhone('1234-5678'); // "12345678"
 * ```
 */
export function cleanPhone(phone: string | null | undefined): string {
  if (!phone) return '';

  try {
    // Eliminar todo excepto números
    let cleaned = phone.replace(/\D/g, '');

    // Eliminar código de país si existe
    if (cleaned.startsWith('502') && cleaned.length > 8) {
      cleaned = cleaned.substring(3);
    }

    return cleaned;
  } catch {
    return '';
  }
}

// ============================================
// FORMATEO DE NÚMEROS
// ============================================

/**
 * Formatea un número con separadores de miles
 * 
 * @param num - Número a formatear
 * @param decimals - Cantidad de decimales (default: 2)
 * @returns Número formateado
 * 
 * @example
 * ```typescript
 * formatNumber(1500); // "1,500"
 * formatNumber(1500.5, 2); // "1,500.50"
 * formatNumber(1500000, 0); // "1,500,000"
 * ```
 */
export function formatNumber(
  num: number | string | null | undefined,
  decimals = 0
): string {
  if (num === null || num === undefined || num === '') {
    return '0';
  }

  try {
    const numValue = typeof num === 'string' ? parseFloat(num) : num;
    
    if (isNaN(numValue)) {
      return '0';
    }

    return numValue.toLocaleString('es-GT', {
      minimumFractionDigits: decimals,
      maximumFractionDigits: decimals,
    });
  } catch {
    return '0';
  }
}

/**
 * Formatea un porcentaje
 * 
 * @param value - Valor a formatear (0-100 o 0-1 según useDecimal)
 * @param decimals - Cantidad de decimales (default: 2)
 * @param useDecimal - Si true, value está entre 0-1 (default: false)
 * @returns Porcentaje formateado
 * 
 * @example
 * ```typescript
 * formatPercentage(75); // "75.00%"
 * formatPercentage(0.75, 2, true); // "75.00%"
 * formatPercentage(75.5, 1); // "75.5%"
 * ```
 */
export function formatPercentage(
  value: number | string | null | undefined,
  decimals = 2,
  useDecimal = false
): string {
  if (value === null || value === undefined || value === '') {
    return '0%';
  }

  try {
    let numValue = typeof value === 'string' ? parseFloat(value) : value;
    
    if (isNaN(numValue)) {
      return '0%';
    }

    // Si useDecimal=true, convertir de 0-1 a 0-100
    if (useDecimal) {
      numValue = numValue * 100;
    }

    return `${numValue.toFixed(decimals)}%`;
  } catch {
    return '0%';
  }
}

// ============================================
// FORMATEO DE DPI Y NIT (GUATEMALA)
// ============================================

/**
 * Formatea un DPI guatemalteco
 * 
 * @param dpi - DPI a formatear (13 dígitos)
 * @returns DPI formateado: #### ##### ####
 * 
 * @example
 * ```typescript
 * formatDPI('1234567890123'); // "1234 56789 0123"
 * ```
 */
export function formatDPI(dpi: string | number | null | undefined): string {
  if (!dpi) return '';

  try {
    // Limpiar y validar
    const cleaned = String(dpi).replace(/\D/g, '');

    if (cleaned.length !== 13) {
      return String(dpi); // Retornar original si no es válido
    }

    // Formatear: #### ##### ####
    return `${cleaned.substring(0, 4)} ${cleaned.substring(4, 9)} ${cleaned.substring(9)}`;
  } catch {
    return String(dpi);
  }
}

/**
 * Formatea un NIT guatemalteco
 * 
 * @param nit - NIT a formatear
 * @returns NIT formateado: ########-#
 * 
 * @example
 * ```typescript
 * formatNIT('123456789'); // "12345678-9"
 * formatNIT('12345678-9'); // "12345678-9" (ya formateado)
 * ```
 */
export function formatNIT(nit: string | number | null | undefined): string {
  if (!nit) return '';

  try {
    // Limpiar
    const cleaned = String(nit).replace(/\D/g, '');

    if (cleaned.length < 2) {
      return String(nit);
    }

    // Formatear: ########-#
    const body = cleaned.substring(0, cleaned.length - 1);
    const check = cleaned.substring(cleaned.length - 1);

    return `${body}-${check}`;
  } catch {
    return String(nit);
  }
}

// ============================================
// FORMATEO DE STRINGS
// ============================================

/**
 * Capitaliza la primera letra de un string
 * 
 * @param str - String a capitalizar
 * @returns String capitalizado
 * 
 * @example
 * ```typescript
 * capitalize('hola mundo'); // "Hola mundo"
 * ```
 */
export function capitalize(str: string | null | undefined): string {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

/**
 * Capitaliza cada palabra de un string
 * 
 * @param str - String a capitalizar
 * @returns String con cada palabra capitalizada
 * 
 * @example
 * ```typescript
 * capitalizeWords('hola mundo ejemplo'); // "Hola Mundo Ejemplo"
 * ```
 */
export function capitalizeWords(str: string | null | undefined): string {
  if (!str) return '';
  
  return str
    .split(' ')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(' ');
}

/**
 * Trunca un string a una longitud específica agregando "..."
 * 
 * @param str - String a truncar
 * @param maxLength - Longitud máxima
 * @returns String truncado
 * 
 * @example
 * ```typescript
 * truncate('Este es un texto muy largo', 10); // "Este es..."
 * ```
 */
export function truncate(
  str: string | null | undefined,
  maxLength: number
): string {
  if (!str) return '';
  
  if (str.length <= maxLength) {
    return str;
  }
  
  return str.substring(0, maxLength - 3) + '...';
}

/**
 * Normaliza un string para búsquedas (elimina acentos, convierte a minúsculas)
 * 
 * @param str - String a normalizar
 * @returns String normalizado
 * 
 * @example
 * ```typescript
 * normalizeString('José María'); // "jose maria"
 * normalizeString('ÑOÑO'); // "nono"
 * ```
 */
export function normalizeString(str: string | null | undefined): string {
  if (!str) return '';
  
  return str
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, ''); // Eliminar diacríticos
}

// ============================================
// FORMATEO DE TAMAÑO DE ARCHIVOS
// ============================================

/**
 * Formatea un tamaño de archivo en bytes a formato legible
 * 
 * @param bytes - Tamaño en bytes
 * @param decimals - Cantidad de decimales (default: 2)
 * @returns Tamaño formateado
 * 
 * @example
 * ```typescript
 * formatFileSize(1024); // "1.00 KB"
 * formatFileSize(1048576); // "1.00 MB"
 * formatFileSize(1500000); // "1.43 MB"
 * ```
 */
export function formatFileSize(
  bytes: number | null | undefined,
  decimals = 2
): string {
  if (!bytes || bytes === 0) return '0 Bytes';

  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));

  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(decimals))} ${sizes[i]}`;
}

// ============================================
// EXPORTACIÓN AGRUPADA
// ============================================

/**
 * Objeto con todas las funciones de formateo
 * Útil para importar todo de una vez
 * 
 * @example
 * ```typescript
 * import { formatHelpers } from '@/utils/format';
 * 
 * formatHelpers.formatCurrency(1500);
 * formatHelpers.formatPhone('12345678');
 * ```
 */
export const formatHelpers = {
  // Fechas
  formatDate,
  formatDateISO,
  formatDateRelative,
  formatTime,
  
  // Moneda
  formatCurrency,
  parseCurrency,
  
  // Teléfonos
  formatPhone,
  cleanPhone,
  
  // Números
  formatNumber,
  formatPercentage,
  
  // DPI/NIT
  formatDPI,
  formatNIT,
  
  // Strings
  capitalize,
  capitalizeWords,
  truncate,
  normalizeString,
  
  // Archivos
  formatFileSize,
};