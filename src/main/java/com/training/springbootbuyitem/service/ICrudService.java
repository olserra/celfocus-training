package com.training.springbootbuyitem.service;

import java.util.List;

public interface ICrudService<T extends Object> {


	List<T> list();

	T get(Long id);

	//TODO ex: 10
	List<T> get(List<Long> id);

	void delete(Long id);

	T update(T entity);

	T save(T entity);

}
