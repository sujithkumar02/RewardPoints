package com.customer.rewards.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.customer.rewards.data.SampleData;
import com.customer.rewards.dto.CustomerRewardsDTO;
import com.customer.rewards.service.CustomerRewardsService;

@Service
public class CustomerRewardsServiceImpl implements CustomerRewardsService{
	
	@Override
	public CustomerRewardsDTO getCustomerRewards(String customer)throws ParseException {
		List<SampleData> sampleData = getSampleData();
		//Calculate points
		List<SampleData> includePoints = new ArrayList<>();
		for(SampleData data:sampleData){
			data.setPoints(caluclatePoints(data));
			includePoints.add(data);
		}

		//get total count
		Map<Object, Long> months = includePoints.stream()
				.filter(c -> c.getCustomerName().equals(customer))
				.collect(Collectors.groupingBy(element -> element.getCreatedDate().getMonth(), Collectors.summingLong(element->element.getPoints())));

		List<CustomerRewardsDTO.MonthDetails> month = new ArrayList<CustomerRewardsDTO.MonthDetails>(); 
		Long total = 0l;
		for (Entry<Object, Long> entry : months.entrySet()) {
			CustomerRewardsDTO.MonthDetails dto = new CustomerRewardsDTO().new MonthDetails();
			dto.setMonth(((Integer)entry.getKey()+1));
			Long monthTotal = entry.getValue();
			total += monthTotal;
			dto.setMonthPoints(monthTotal);
			
			month.add(dto);
		}
		
		CustomerRewardsDTO customerDetails = new CustomerRewardsDTO();
		customerDetails.setTotal(total);
		customerDetails.setMonthDetails(month);
		return customerDetails;
	}
	//caluclate points
	private Integer caluclatePoints(SampleData data) {
		Integer amount = data.getPurchaseAmount();
		return amount>=100 ?  50+(amount%100)*2 : amount>=50 ? (amount-50)*1 : 0;
	}

	//sample data
	private List<SampleData> getSampleData() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		SampleData customer1 = new SampleData();
		customer1.setCustomerName("customer1");
		customer1.setPurchaseAmount(130);
		customer1.setCreatedDate(sdf.parse("20/03/2020"));
		
		SampleData customer2 = new SampleData();
		customer2.setCustomerName("customer1");
		customer2.setPurchaseAmount(120);
		customer2.setCreatedDate(sdf.parse("22/03/2020"));
		
		SampleData customer3 = new SampleData();
		customer3.setCustomerName("customer1");
		customer3.setPurchaseAmount(100);
		customer3.setCreatedDate(sdf.parse("10/04/2020"));
		
		// 2nd Customer
		SampleData c1 = new SampleData();
		c1.setCustomerName("customer2");
		c1.setPurchaseAmount(70);
		c1.setCreatedDate(sdf.parse("23/03/2020"));
		
		SampleData c2 = new SampleData();
		c2.setCustomerName("customer2");
		c2.setPurchaseAmount(140);
		c2.setCreatedDate(sdf.parse("12/04/2020"));
		
		List<SampleData> list = new ArrayList<>();
		list.add(customer1);
		list.add(customer2);
		list.add(customer3);
		list.add(c1);
		list.add(c2);
		
		return list;
	}
}
