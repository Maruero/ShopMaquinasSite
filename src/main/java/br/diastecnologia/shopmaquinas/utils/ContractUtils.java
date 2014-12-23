package br.diastecnologia.shopmaquinas.utils;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.enums.ContractDefinitionProperty;
import br.diastecnologia.shopmaquinas.enums.ContractStatus;

public class ContractUtils {

	public static Contract getValidContract( List<Contract> contracts ){
		Contract contract = null;
		if( contracts != null && contracts.size() > 0 ){
		
			Optional<Contract> opContract = contracts.stream().filter( c-> c.getEndDate() == null || c.getEndDate().after(Calendar.getInstance().getTime()) ).findFirst();
			if( opContract.isPresent()){
				contract = opContract.get();
				
				double maxAd = contract.getContractDefinition()
						.getContractDefinitionPropertyValues()
						.stream().filter( p -> 
							p.getContractDefinitionProperty().getName().equals( ContractDefinitionProperty.AD_QUANTITY.toString() ) 
						).findFirst().get().getDoubleValue();
				
				int contractAdsCount = 0;
				if( contract.getAds() != null){
					contractAdsCount = contract.getAds().size();
				}
				
				if( maxAd <= contractAdsCount ){
					contract = null;
				}
				
				if( contract.getContractStatus() != ContractStatus.ACTIVE){
					contract = null;
				}
			}
		}
		return contract;
	}
	
}
