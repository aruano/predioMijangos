package com.predio.mijangos.modules.compras.service;

import com.predio.mijangos.modules.compras.dto.*;
import org.springframework.data.domain.*;

/**
 * Servicio de negocio para gestión de proveedores. Orquesta validaciones y
 * persistencia sin exponer entidades al exterior.
 */
public interface ProveedorService {

    Page<ProveedorListItemDTO> list(String q, Pageable pageable);

    ProveedorResponseDTO create(ProveedorCreateDTO dto);

    ProveedorResponseDTO update(ProveedorUpdateDTO dto);

    void toggleActivo(Integer id, boolean activo);

    ProveedorResponseDTO getById(Integer id);
}
