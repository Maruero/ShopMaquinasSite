package br.diastecnologia.shopmaquinas.utils;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.enums.ContractDefinitionProperty;

public class ContractUtils {

	public static Contract getValidContract( List<Contract> contracts ){
		Contract contract = null;
		if( contracts != null && contracts.size() > 0 ){
		
			Optional<Contract> opContract = contracts.stream().filter( c-> c.getEndDate().after(Calendar.getInstance().getTime()) ).findFirst();
			if( opContract.isPresent()){
				contract = opContract.get();
				
				double maxAd = contract.getContractDefinition()
						.getContractDefinitionPropertyValues()
						.stream().filter( p -> 
							p.getContractDefinitionProperty().getName().equals( ContractDefinitionProperty.AD_QUANTITY.toString() ) 
						).findFirst().get().getDoubleValue();
				if( maxAd <= contract.getAds().size() ){
					contract = null;
				}
			}
		}
		return contract;
	}
	
}
