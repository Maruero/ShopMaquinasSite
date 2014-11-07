package br.diastecnologia.shopmaquinas.utils;

import java.util.Calendar;
import java.util.Optional;

import br.diastecnologia.shopmaquinas.bean.Contract;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.enums.ContractDefinitionProperty;

public class ContractUtils {

	public static Contract getValidContract( Person person ){
		Contract contract = null;
		if( person != null && person.getContracts() != null && person.getContracts().size() > 0 ){
		
			Optional<Contract> opContract = person.getContracts().stream().filter( c-> c.getEndDate().before(Calendar.getInstance().getTime()) ).findFirst();
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
