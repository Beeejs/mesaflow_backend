package com.mesaflow.mesaflow_api.Controller;

import com.mesaflow.mesaflow_api.DTOs.AccionReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.AccionReservaResponse;
import com.mesaflow.mesaflow_api.DTOs.ApiResponse;
import com.mesaflow.mesaflow_api.DTOs.ConsultarReservasRequest;
import com.mesaflow.mesaflow_api.DTOs.CrearReservaRequest;
import com.mesaflow.mesaflow_api.DTOs.ReservaResponse;
import com.mesaflow.mesaflow_api.Service.ReservaService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// Swagger
@Tag(
  name = "Reservas",
  description = "Endpoints para crear, confirmar, cancelar y consultar reservas."
)
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

  private final ReservaService reservaService;

  public ReservaController(ReservaService reservaService) {
    this.reservaService = reservaService;
  }

  // Swagger
  @Operation(
    summary = "1. Crear reserva",
    description = """
      Crea una nueva reserva en estado PENDIENTE.

      Validaciones principales:
      - El usuario debe existir y estar activo.
      - El establecimiento debe existir, estar activo y permitir reservas.
      - La fecha y hora de inicio debe ser futura.
      - La cantidad de comensales debe ser mayor a cero.
      - El usuario no debe tener otra reserva activa ese mismo día.
      - Debe existir configuración activa de reservas para el establecimiento.
      - No debe superarse el porcentaje máximo de ocupación configurado.
      - Debe haber una mesa o combinación de mesas disponible.
      """
  )
  @ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Reserva creada correctamente.",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": true,
              "response": {
                "idReserva": 31,
                "idUsuario": 1,
                "nombreUsuario": "Juan Perez",
                "idEstablecimiento": 15,
                "nombreEstablecimiento": "Burger Station",
                "fechaHoraInicio": "2026-07-21T21:00:00",
                "fechaHoraFinCalculada": "2026-07-21T23:00:00",
                "comensales": 4,
                "estado": 0,
                "estadoDescripcion": "PENDIENTE",
                "mesasAsignadas": [
                  "BS4"
                ]
              }
            }
            """
        )
      )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "400",
      description = """
        Error de validación o regla de negocio.

        Posibles causas:
        - Falta idUsuario.
        - Falta idEstablecimiento.
        - Falta fechaHoraInicio.
        - La fecha de reserva no es futura.
        - Falta comensales.
        - La cantidad de comensales no es mayor a cero.
        - El usuario no existe o no está activo.
        - El establecimiento no existe, no está activo o no permite reservas.
        - El usuario ya tiene una reserva activa para ese día.
        - No hay mesas disponibles.
        - Se supera la ocupación máxima permitida.
        - El JSON contiene campos desconocidos.
        """,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": false,
              "message": "El usuario ya tiene una reserva activa para ese día."
            }
            """
        )
      )
    )
  })
  // Crear Reserva
  @PostMapping
  public ResponseEntity<ApiResponse<ReservaResponse>> crearReserva(
    @Valid @RequestBody CrearReservaRequest request
  ) {
    ReservaResponse reservaCreada = reservaService.crearReserva(request);

    return ResponseEntity.ok(ApiResponse.ok(reservaCreada));
  }

  // Swagger
  @Operation(
    summary = "2. Ejecutar acción sobre una reserva",
    description = """
      Ejecuta una acción sobre una reserva existente.

      Scopes válidos:
      - CONFIRMAR: cambia una reserva PENDIENTE a CONFIRMADA.
      - CANCELAR: cambia una reserva PENDIENTE o CONFIRMADA a CANCELADA.

      Reglas:
      - Para confirmar, el usuario debe ser administrador activo del establecimiento.
      - Para cancelar, puede hacerlo el cliente dueño de la reserva o el administrador activo del establecimiento.
      - Una reserva CANCELADA no puede volver a confirmarse ni cancelarse nuevamente.
      """
  )
  @ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Acción ejecutada correctamente.",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": true,
              "response": {
                "idReserva": 31,
                "estado": 1,
                "estadoDescripcion": "CONFIRMADA"
              }
            }
            """
        )
      )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "400",
      description = """
        Error de validación, permisos o regla de negocio.

        Posibles causas:
        - Falta idUsuario.
        - Falta scope.
        - El scope indicado no es válido.
        - La reserva no existe.
        - El usuario no existe o no está activo.
        - El usuario no tiene permisos para confirmar reservas.
        - El usuario no tiene permisos para cancelar reservas.
        - El cliente intenta cancelar una reserva ajena.
        - El usuario no es administrador del establecimiento de la reserva.
        - Solo se pueden confirmar reservas pendientes.
        - Solo se pueden cancelar reservas pendientes o confirmadas.
        - El establecimiento no permite cancelar reservas.
        - La reserva superó el tiempo límite de cancelación.
        """,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": false,
              "message": "La acción indicada no es válida."
            }
            """
        )
      )
    )
  })
  // Ejecutar acción sobre una reserva (confirmar, cancelar)
  @PutMapping("/{idReserva}/accion")
  public ResponseEntity<ApiResponse<AccionReservaResponse>> ejecutarAccionReserva(
      @PathVariable Integer idReserva,
      @Valid @RequestBody AccionReservaRequest request
  ) {
    AccionReservaResponse reservaActualizada = reservaService.ejecutarAccionReserva(
      idReserva,
      request
    );

    return ResponseEntity.ok(ApiResponse.ok(reservaActualizada));
  }

  // Swagger
  @Operation(
    summary = "3. Consultar reservas por establecimiento",
    description = """
      Devuelve las reservas asociadas a un establecimiento.

      Reglas:
      - Debe enviarse idUsuarioSolicitante.
      - Debe enviarse idEstablecimiento.
      - El usuario solicitante debe existir y estar activo.
      - El usuario solicitante debe ser ADMIN_ESTABLECIMIENTO.
      - El usuario solicitante debe estar asociado como ADMIN activo del establecimiento.
      - Permite filtrar por estado.

      Estados permitidos en filtro.estado:
      - todas
      - pendiente
      - confirmada
      - cancelada
      - no_show
      """
  )
  @ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Reservas obtenidas correctamente.",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": true,
              "response": [
                {
                  "idReserva": 31,
                  "idUsuario": 1,
                  "nombreUsuario": "Juan Perez",
                  "idEstablecimiento": 15,
                  "nombreEstablecimiento": "Burger Station",
                  "fechaHoraInicio": "2026-07-21T21:00:00",
                  "fechaHoraFinCalculada": "2026-07-21T23:00:00",
                  "comensales": 4,
                  "estado": 0,
                  "estadoDescripcion": "PENDIENTE",
                  "mesasAsignadas": [
                    "BS4"
                  ]
                }
              ]
            }
            """
        )
      )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "400",
      description = """
        Error de validación, permisos o filtro inválido.

        Posibles causas:
        - Falta idUsuarioSolicitante.
        - Falta idEstablecimiento.
        - El usuario solicitante no existe o no está activo.
        - El usuario no tiene permisos para gestionar reservas de establecimientos.
        - El usuario no es administrador del establecimiento.
        - El filtro de estado indicado no es válido.
        """,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": false,
              "message": "El usuario no es administrador del establecimiento."
            }
            """
        )
      )
    )
  })
  // Consultar reservas por establecimiento
  @PostMapping("/consultar/establecimiento")
  public ResponseEntity<ApiResponse<List<ReservaResponse>>> consultarReservasPorEstablecimiento(
    @Valid @RequestBody ConsultarReservasRequest request
  ) {
    List<ReservaResponse> reservas = reservaService.consultarReservasPorEstablecimiento(request);

    return ResponseEntity.ok(ApiResponse.ok(reservas));
  }

  // Swagger
  @Operation(
    summary = "4. Consultar reservas por usuario",
    description = """
      Devuelve las reservas asociadas a un usuario.

      Reglas:
      - Debe enviarse idUsuarioSolicitante.
      - Debe enviarse idUsuario.
      - El usuario solicitante debe existir y estar activo.
      - El usuario consultado debe existir y estar activo.
      - Por ahora, el usuario solicitante debe coincidir con el usuario consultado.
      - Permite filtrar por estado.

      Estados permitidos en filtro.estado:
      - todas
      - pendiente
      - confirmada
      - cancelada
      - no_show
      """
  )
  @ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Reservas obtenidas correctamente.",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": true,
              "response": [
                {
                  "idReserva": 31,
                  "idUsuario": 1,
                  "nombreUsuario": "Juan Perez",
                  "idEstablecimiento": 15,
                  "nombreEstablecimiento": "Burger Station",
                  "fechaHoraInicio": "2026-07-21T21:00:00",
                  "fechaHoraFinCalculada": "2026-07-21T23:00:00",
                  "comensales": 4,
                  "estado": 0,
                  "estadoDescripcion": "PENDIENTE",
                  "mesasAsignadas": [
                    "BS4"
                  ]
                }
              ]
            }
            """
        )
      )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "400",
      description = """
        Error de validación, permisos o filtro inválido.

        Posibles causas:
        - Falta idUsuarioSolicitante.
        - Falta idUsuario.
        - El usuario solicitante no existe o no está activo.
        - El usuario consultado no existe o no está activo.
        - El usuario solo puede consultar sus propias reservas.
        - El filtro de estado indicado no es válido.
        """,
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = """
            {
              "success": false,
              "message": "El usuario solo puede consultar sus propias reservas."
            }
            """
        )
      )
    )
  })
  // Consultar reservas por usuario
  @PostMapping("/consultar/usuario")
  public ResponseEntity<ApiResponse<List<ReservaResponse>>> consultarReservasPorUsuario(
    @Valid @RequestBody ConsultarReservasRequest request
  ) {
    List<ReservaResponse> reservas = reservaService.consultarReservasPorUsuario(request);

    return ResponseEntity.ok(ApiResponse.ok(reservas));
  }
}