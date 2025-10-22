// ==================== PERSONA SERVICE ====================

package com.predio.mijangos.modules.usuarios.service;

import com.predio.mijangos.modules.usuarios.dto.persona.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonaService {
    PersonaResponseDTO findById(Integer id);
    PersonaResponseDTO findByIdentificacion(String identificacion);
    Page<PersonaListDTO> findAll(Pageable pageable);
    PersonaResponseDTO create(PersonaCreateDTO dto);
    PersonaResponseDTO update(Integer id, PersonaUpdateDTO dto);
    void delete(Integer id);
}
