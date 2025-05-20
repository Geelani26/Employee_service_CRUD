package com.example.HRMS.utils;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Service
public class PageingUtil {

	public Pageable getPageable(Integer page, Integer pageSize, String order, String field) {

		if (page == null && pageSize == null) {
			throw new IllegalArgumentException("page_no and page_size must required");
		}
		if ((page != null && page <= 0) || (pageSize != null && pageSize <= 0)) {
			// implemenet custom Exception
			throw new IllegalArgumentException("page_no or page_size have invalid data");
		}
		if((order != null && field == null )|| (order ==null && field != null)) {
			throw new IllegalArgumentException("both sort_as and sort_by field required to sort");
		}

		Sort sort = null;
		Pageable pageable = null;

		if (order != null && field != null && page != null && pageSize != null) {

			if (order != null && "asc".equalsIgnoreCase(order)) {
				if (field.contains("_")) {
					// Convert snake_case to camelCase
					field = snakeCaseToCamelCase(field);
				}
				sort = Sort.by(field).ascending();
			} else {
				if (field.contains("_")) {
					// Convert snake_case to camelCase
					field = snakeCaseToCamelCase(field);
				}
				sort = Sort.by(field).descending();
			}
			pageable = PageRequest.of(page - 1, pageSize, sort);
		}
		if (page != null && pageSize != null && order == null && field == null) {
			pageable = PageRequest.of(page - 1, pageSize);
		}
		return pageable;
	}

	private String snakeCaseToCamelCase(String input) {
		String[] parts = input.split("_");
		StringBuilder camelCase = new StringBuilder(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			camelCase.append(Character.toUpperCase(parts[i].charAt(0))).append(parts[i].substring(1));
		}
		System.out.println(camelCase.toString());
		return camelCase.toString();
	}

//	public static <T extends AlyssaModel> Page<T> getSelectedIdResponse(String selectedIdString, Page<T> requestData, Function<Long, T> func) {
//		Pageable pageable = requestData.getPageable();
//		List<Long> selectedIds = FieldSetConvertionUtil.getListLongForSelectedId(selectedIdString);
//		if (!selectedIds.isEmpty()) {
//			List<T> rawList = new LinkedList<>(requestData.getContent());
//			List<T> preList = new LinkedList<>();
//
//			for (Long selectedId : selectedIds) {
//				boolean elementFound = false;
//				for (T item : rawList) {
//					if(item.getId().equals(selectedId)){
//						preList.add(item);
//						elementFound = true;
//						break;
//					}
//				}
//
//				if(elementFound) {
//					rawList = rawList.stream().filter(obj -> !obj.getId().equals(selectedId)).toList();
//				} else {
//					T obj = func.apply(selectedId);
//					if(obj != null){
//						preList.add(obj);
//					}
//				}
//			}
//
//			preList.addAll(rawList);
//			List<T> responseList = new LinkedList<>(preList);
//			if (responseList.size() > pageable.getPageSize()) {
//				responseList = responseList.subList(0, pageable.getPageSize());
//			}
//			return new PageImpl<>(responseList, pageable, requestData.getTotalElements());
//		} else {
//			return requestData;
//		}
//	}
//
//	public static <T> PageImpl<T> paginateList(List<T> list, Pageable pageable) {
//		int start = Math.min((int)pageable.getOffset(), list.size());
//		int end = Math.min((start + pageable.getPageSize()), list.size());
//		List<T> subList = list.subList(start, end);
//		return new PageImpl<>(subList, pageable, list.size());
//	}

}
