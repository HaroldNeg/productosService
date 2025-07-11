package com.linktic.prueba.productos.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiResponse<T> {
	private List<T> data;
	private Meta meta;
	private Links links;
	
	@Getter
	@AllArgsConstructor
	public static class Meta {
		private int page;
		private int size;
		private long totalElements;
		private int totalPages;
	}
	
	@Getter
	@AllArgsConstructor
	public static class Links {
		private String self;
        private String next;
        private String prev;
	}

}
