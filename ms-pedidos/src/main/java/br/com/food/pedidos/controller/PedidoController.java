package br.com.food.pedidos.controller;

import br.com.food.pedidos.dto.PedidoDto;
import br.com.food.pedidos.dto.StatusDto;
import br.com.food.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping("/porta")
    public String retornaPorta(@Value("${local.server.port}") String porta){
        return String.format("Requisição respondida pela instância executando na porta %s", porta);
    }

    @GetMapping()
    public List<PedidoDto> listarTodos() {
        return service.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> listarPorId(@PathVariable @NotNull Long id) {
        PedidoDto dto = service.obterPorId(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping()
    public ResponseEntity<PedidoDto> realizaPedido(@RequestBody @Valid PedidoDto dto, UriComponentsBuilder uriBuilder) {
        PedidoDto pedidoRealizado = service.criarPedido(dto);

        URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.getId()).toUri();

        return ResponseEntity.created(endereco).body(pedidoRealizado);

    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDto> atualizaStatus(@PathVariable Long id, @RequestBody StatusDto status) {
        PedidoDto dto = service.atualizaStatus(id, status);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/pago")
    public ResponseEntity<Void> aprovaPagamento(@PathVariable @NotNull Long id) {
        service.aprovaPagamentoPedido(id);

        return ResponseEntity.ok().build();

    }
}
