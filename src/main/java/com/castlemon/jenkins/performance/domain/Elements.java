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
@JsonPropertyOrder({ "id", "tags", "description", "name", "keyword", "line",
		"steps", "type" })
public class Elements {

	@JsonProperty("id")
	private String id;

	@JsonProperty("tags")
	private List<Tag> tags = new ArrayList<Tag>();

	@JsonProperty("description")
	private String description;

	@JsonProperty("name")
	private String name;

	@JsonProperty("keyword")
	private String keyword;

	@JsonProperty("line")
	private Integer line;

	@JsonProperty("steps")
	private List<Step> steps = new ArrayList<Step>();

	@JsonProperty("type")
	private String type;

	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("tags")
	public List<Tag> getTags() {
		return tags;
	}

	@JsonProperty("tags")
	public void setTags(List<Tag> tags) {
		this.tags = tags;
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

	@JsonProperty("steps")
	public List<Step> getSteps() {
		return steps;
	}

	@JsonProperty("steps")
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
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
