package br.com.biblioteca.controllers.dto;

public record CreateLogradouroRequest(
        String rua,
        int numero,
        String cidade,
        String CEP,
        String complemento,
        String ibge,
        String uf,
        String bairro) {
}
