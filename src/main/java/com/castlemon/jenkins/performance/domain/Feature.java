package com.castlemon.jenkins.performance.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({ "id", "description", "name", "keyword", "line",
		"elements", "uri" })
public class Feature {

	@JsonProperty("id")
	private String id;
	@JsonProperty("description")
	private String description;
	@JsonProperty("name")
	private String name;
	@JsonProperty("keyword")
	private String keyword;
	@JsonProperty("line")
	private Integer line;
	@JsonProperty("elements")
	private List<Elements> elements = new ArrayList<Elements>();
	@JsonProperty("uri")
	private String uri;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("keyword")
	public String getKeyword() {
		return keyword;
	}

	@JsonProperty("keyword")
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@JsonProperty("line")
	public Integer getLine() {
		return line;
	}

	@JsonProperty("line")
	public void setLine(Integer line) {
		this.line = line;
	}

	@JsonProperty("elements")
	public List<Elements> getElements() {
		return elements;
	}

	@JsonProperty("elements")
	public void setElements(List<Elements> elements) {
		this.elements = elements;
	}

	@JsonProperty("uri")
	public String getUri() {
		return uri;
	}

	@JsonProperty("uri")
	public void setUri(String uri) {
		this.uri = uri;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperties(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
