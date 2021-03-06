package com.training.springbootbuyitem.service;

import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.enums.EnumItemState;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.error.StockNotAvailableException;
import com.training.springbootbuyitem.repository.ItemRepository;
import com.training.springbootbuyitem.utils.properties.ItemStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService implements IItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemStorageProperties itemStorageProperties;

	/**
	 * @JavaDoc RestTemplate is a synchronous Http Client which is supported by Pivotal development team take into
	 * consideration this client is deprecated and shall not be supported for LTS use instead the newly Http Client
	 * WebClient which is capable of synchronous & asynchronous invocations check some code samples at:
	 * https://spring.io/guides/gs/consuming-rest/
	 */
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Item> list() {
		return itemRepository.findAll();
	}

	@Override
	public Item get(Long id) {
		return itemRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException(EnumEntity.ITEM.name(), id));
	}

	@Override
	public List<Item> get(List<Long> id) {
		return new ArrayList<>();
	}

	@Override
	public void delete(Long id) {
		itemRepository.delete(get(id));
	}


	@Override
	public Item update(Item item) {
		Item persistedItem = get(item.getItemUid());
		if (!StringUtils.isEmpty(item.getName())) {
			persistedItem.setName(item.getName());
		}
		if (!StringUtils.isEmpty(item.getDescription())) {
			persistedItem.setDescription(item.getDescription());
		}
		if (!StringUtils.isEmpty(item.getMarket())) {
			persistedItem.setMarket(item.getMarket());
		}
		if (item.getStock() != null && item.getStock().intValue() >= 0) {
			persistedItem.setStock(item.getStock());
		}
		if (item.getPriceTag() != null && item.getPriceTag().longValue() >= 0.0) {
			persistedItem.setPriceTag(item.getPriceTag());
		}
		return itemRepository.save(persistedItem);
	}

	@Override
	public Item save(Item item) {
		item.setState(EnumItemState.AVAILABLE.name());
		return itemRepository.save(item);
	}

	@Override
	public void restock(Long id, Integer quantity) {
		// TODO
	}

	@Override
	public void dispatch(Long id, Integer quantity) {
		Item item = get(id);
		checkItemAvailability(item, quantity);
		item.setStock(item.getStock().subtract(BigInteger.valueOf(quantity)));
		item.setReservedStock(item.getStock().subtract(BigInteger.valueOf(quantity)));
		save(item);
	}

	private void checkItemAvailability(Item item, Integer quantity) {
		if (item.getStock().compareTo(BigInteger.valueOf(quantity)) < 0) {
			throw new StockNotAvailableException(item.getName());
		}
	}

	@Override
	public void block(Long id, Integer quantity) {
		Item item = get(id);
		item.setStock(item.getStock().subtract(BigInteger.valueOf(quantity)));
		item.setReservedStock(item.getReservedStock().add(BigInteger.valueOf(quantity)));
		save(item);
	}
}
