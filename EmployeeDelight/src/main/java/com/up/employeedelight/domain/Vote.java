package com.up.employeedelight.domain;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the votes database table.
 * 
 */
@Entity
@Table(name = "votes")
@NamedQuery(name = "Vote.findAll", query = "SELECT v FROM Vote v")
public class Vote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date creationDate;

	private Boolean isSubmitted;

	private Integer points;

	// uni-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	// uni-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;

	public Vote() {
	}

	public Vote(Date creationDate, Integer points, User user, Product product) {
		super();
		this.creationDate = creationDate;
		this.points = points;
		this.user = user;
		this.product = product;
		this.isSubmitted = false;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getIsSubmitted() {
		return this.isSubmitted;
	}

	public void setIsSubmitted(Boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	public Integer getPoints() {
		return this.points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}