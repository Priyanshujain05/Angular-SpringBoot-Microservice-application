package com.encrypted.secured.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchResponse {
	@JsonProperty("data")
	private List<Match> data;

	@JsonProperty("total")
	private int total;

	@JsonProperty("page")
	private int page;

	@JsonProperty("per_page")
	private int perPage;

	@JsonProperty("total_pages")
	private int totalPages;

	public List<Match> getData() {
		return data;
	}

	public void setData(List<Match> data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
