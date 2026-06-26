package com.mesaflow.mesaflow_api;

import com.mesaflow.mesaflow_api.Controller.ReservaController;
import com.mesaflow.mesaflow_api.DTOs.AccionReservaResponse;
import com.mesaflow.mesaflow_api.DTOs.ReservaResponse;
import com.mesaflow.mesaflow_api.Service.ReservaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ReservaService reservaService;

  // Test para consultar reservas por usuario - OK
  @Test
  @DisplayName("Debe consultar reservas por usuario correctamente")
  void consultarReservasPorUsuario_ok() throws Exception {
    ReservaResponse reservaResponse = new ReservaResponse(
      31,
      1,
      "Juan Perez",
      15,
      "Burger Station",
      LocalDateTime.of(2026, 7, 19, 21, 0),
      LocalDateTime.of(2026, 7, 19, 23, 0),
      4,
      0,
      "PENDIENTE",
      List.of("BS4")
    );

    Mockito.when(reservaService.consultarReservasPorUsuario(any()))
      .thenReturn(List.of(reservaResponse));

    String requestJson = """
      {
        "idUsuarioSolicitante": 1,
        "idUsuario": 1,
        "filtro": {
          "estado": "todas"
        }
      }
    """;

    mockMvc.perform(post("/api/reservas/consultar/usuario")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.response[0].idReserva").value(31))
      .andExpect(jsonPath("$.response[0].idUsuario").value(1))
      .andExpect(jsonPath("$.response[0].nombreUsuario").value("Juan Perez"))
      .andExpect(jsonPath("$.response[0].idEstablecimiento").value(15))
      .andExpect(jsonPath("$.response[0].nombreEstablecimiento").value("Burger Station"))
      .andExpect(jsonPath("$.response[0].estado").value(0))
      .andExpect(jsonPath("$.response[0].estadoDescripcion").value("PENDIENTE"))
      .andExpect(jsonPath("$.response[0].mesasAsignadas[0]").value("BS4"));
  }

  // Test para consultar reservas por establecimiento - OK
  @Test
  @DisplayName("Debe consultar reservas por establecimiento correctamente")
  void consultarReservasPorEstablecimiento_ok() throws Exception {
    ReservaResponse reservaResponse = new ReservaResponse(
      32,
      8,
      "Martin Suarez",
      15,
      "Burger Station",
      LocalDateTime.of(2026, 7, 20, 21, 0),
      LocalDateTime.of(2026, 7, 20, 23, 0),
      6,
      1,
      "CONFIRMADA",
      List.of("BS4")
    );

    Mockito.when(reservaService.consultarReservasPorEstablecimiento(any()))
      .thenReturn(List.of(reservaResponse));

    String requestJson = """
      {
        "idUsuarioSolicitante": 14,
        "idEstablecimiento": 15,
        "filtro": {
          "estado": "todas"
        }
      }
    """;

    mockMvc.perform(post("/api/reservas/consultar/establecimiento")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.response[0].idReserva").value(32))
      .andExpect(jsonPath("$.response[0].idUsuario").value(8))
      .andExpect(jsonPath("$.response[0].nombreUsuario").value("Martin Suarez"))
      .andExpect(jsonPath("$.response[0].idEstablecimiento").value(15))
      .andExpect(jsonPath("$.response[0].nombreEstablecimiento").value("Burger Station"))
      .andExpect(jsonPath("$.response[0].comensales").value(6))
      .andExpect(jsonPath("$.response[0].estado").value(1))
      .andExpect(jsonPath("$.response[0].estadoDescripcion").value("CONFIRMADA"))
      .andExpect(jsonPath("$.response[0].mesasAsignadas[0]").value("BS4"));
  }

  // Test para ejecutar acción sobre una reserva - OK
  @Test
  @DisplayName("Debe confirmar una reserva correctamente")
  void confirmarReserva_ok() throws Exception {
    AccionReservaResponse accionResponse = new AccionReservaResponse(
      31,
      1,
      "CONFIRMADA"
    );

    Mockito.when(reservaService.ejecutarAccionReserva(Mockito.eq(31), any()))
      .thenReturn(accionResponse);

    String requestJson = """
      {
        "idUsuario": 14,
        "scope": "CONFIRMAR"
      }
    """;

    mockMvc.perform(put("/api/reservas/31/accion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.response.idReserva").value(31))
      .andExpect(jsonPath("$.response.estado").value(1))
      .andExpect(jsonPath("$.response.estadoDescripcion").value("CONFIRMADA"));
  }

  // Test para ejecutar acción sobre una reserva - OK
  @Test
  @DisplayName("Debe cancelar una reserva correctamente")
  void cancelarReserva_ok() throws Exception {
    AccionReservaResponse accionResponse = new AccionReservaResponse(
      31,
      2,
      "CANCELADA"
    );

    Mockito.when(reservaService.ejecutarAccionReserva(Mockito.eq(31), any()))
      .thenReturn(accionResponse);

    String requestJson = """
      {
        "idUsuario": 1,
        "scope": "CANCELAR"
      }
    """;

    mockMvc.perform(put("/api/reservas/31/accion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.response.idReserva").value(31))
      .andExpect(jsonPath("$.response.estado").value(2))
      .andExpect(jsonPath("$.response.estadoDescripcion").value("CANCELADA"));
  }

  // Test para crear una reserva - OK
  @Test
  @DisplayName("Debe crear una reserva correctamente")
  void crearReserva_ok() throws Exception {
    ReservaResponse reservaResponse = new ReservaResponse(
      40,
      1,
      "Juan Perez",
      15,
      "Burger Station",
      LocalDateTime.of(2026, 7, 21, 21, 0),
      LocalDateTime.of(2026, 7, 21, 23, 0),
      4,
      0,
      "PENDIENTE",
      List.of("BS4")
    );

    Mockito.when(reservaService.crearReserva(any()))
      .thenReturn(reservaResponse);

    String requestJson = """
      {
        "idUsuario": 1,
        "idEstablecimiento": 15,
        "fechaHoraInicio": "2026-07-21T21:00:00",
        "comensales": 4
      }
    """;

    mockMvc.perform(post("/api/reservas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.response.idReserva").value(40))
      .andExpect(jsonPath("$.response.idUsuario").value(1))
      .andExpect(jsonPath("$.response.nombreUsuario").value("Juan Perez"))
      .andExpect(jsonPath("$.response.idEstablecimiento").value(15))
      .andExpect(jsonPath("$.response.nombreEstablecimiento").value("Burger Station"))
      .andExpect(jsonPath("$.response.comensales").value(4))
      .andExpect(jsonPath("$.response.estado").value(0))
      .andExpect(jsonPath("$.response.estadoDescripcion").value("PENDIENTE"))
      .andExpect(jsonPath("$.response.mesasAsignadas[0]").value("BS4"));
  }

  // Test para crear una reserva sin usuario - ERROR
  @Test
  @DisplayName("Debe devolver error al crear reserva sin usuario")
  void crearReserva_sinIdUsuario_devuelveBadRequest() throws Exception {
    String requestJson = """
      {
        "idEstablecimiento": 15,
        "fechaHoraInicio": "2026-07-21T21:00:00",
        "comensales": 4
      }
    """;

    mockMvc.perform(post("/api/reservas")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("Debe indicar un usuario."));
  }

  // Test para ejecutar acción sobre una reserva sin scope - ERROR
  @Test
  @DisplayName("Debe devolver error al ejecutar acción sin scope")
  void accionReserva_sinScope_devuelveBadRequest() throws Exception {
    String requestJson = """
      {
        "idUsuario": 14
      }
    """;

    mockMvc.perform(put("/api/reservas/31/accion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("Debe indicar la acción a realizar."));
  }

  // Test para consultar reservas por usuario sin usuario solicitante - ERROR
  @Test
  @DisplayName("Debe devolver error al consultar reservas por usuario sin usuario solicitante")
  void consultarReservasPorUsuario_sinIdUsuarioSolicitante_devuelveBadRequest() throws Exception {
    String requestJson = """
      {
        "idUsuario": 1,
        "filtro": {
          "estado": "todas"
        }
      }
    """;

    mockMvc.perform(post("/api/reservas/consultar/usuario")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("Debe indicar el usuario solicitante."));
  }

  // Test para ejecutar acción sobre una reserva con scope inválido - ERROR
  @Test
  @DisplayName("Debe devolver error cuando la acción indicada no es válida")
  void accionReserva_scopeInvalido_devuelveError() throws Exception {
    Mockito.when(reservaService.ejecutarAccionReserva(Mockito.eq(31), any()))
      .thenThrow(new RuntimeException("La acción indicada no es válida."));

    String requestJson = """
      {
        "idUsuario": 14,
        "scope": "PEPE"
      }
    """;

    mockMvc.perform(put("/api/reservas/31/accion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("La acción indicada no es válida."));
  }

  // Test para consultar reservas por usuario con usuario ajeno - ERROR
  @Test
  @DisplayName("Debe devolver error cuando un usuario intenta consultar reservas ajenas")
  void consultarReservasPorUsuario_usuarioAjeno_devuelveError() throws Exception {
    Mockito.when(reservaService.consultarReservasPorUsuario(any()))
      .thenThrow(new RuntimeException("El usuario solo puede consultar sus propias reservas."));

    String requestJson = """
      {
        "idUsuarioSolicitante": 1,
        "idUsuario": 8,
        "filtro": {
          "estado": "todas"
        }
      }
    """;

    mockMvc.perform(post("/api/reservas/consultar/usuario")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("El usuario solo puede consultar sus propias reservas."));
  }

  // Test para consultar reservas por establecimiento con usuario no administrador - ERROR
  @Test
  @DisplayName("Debe devolver error cuando el filtro de estado no es válido")
  void consultarReservasPorUsuario_filtroEstadoInvalido_devuelveError() throws Exception {
    Mockito.when(reservaService.consultarReservasPorUsuario(any()))
      .thenThrow(new RuntimeException("El filtro de estado indicado no es válido."));

    String requestJson = """
      {
        "idUsuarioSolicitante": 1,
        "idUsuario": 1,
        "filtro": {
          "estado": "pepe"
        }
      }
    """;

    mockMvc.perform(post("/api/reservas/consultar/usuario")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.success").value(false))
      .andExpect(jsonPath("$.message").value("El filtro de estado indicado no es válido."));
  }
}