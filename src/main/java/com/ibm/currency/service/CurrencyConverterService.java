package com.ibm.currency.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CoreModel;
import com.ibm.currency.model.CoreResponseModel;
import com.ibm.currency.model.CurrencyConversionFactor;
import com.ibm.currency.repo.CurrencyConverterRepository;

@Service
public class CurrencyConverterService{
	
	@Autowired
	private CurrencyConverterRepository  currencyRepo;
	
	@Autowired
	private CoreResponseModel respModel;	

	
	private ResponseEntity<?>  respEntity;
	
	private static Logger log = LoggerFactory.getLogger(CurrencyConverterService.class);
	
	public ResponseEntity<?>  createConversionfactor(CurrencyConversionFactor ccf){
		try {
			CurrencyConversionFactor obj = currencyRepo.save(ccf);		
			log.info(" Recordcreated Successfully");
			return populateSuccessResponseWithResult(obj, "Successfully saved records to database");
		} catch (Exception ex) {
			log.info(" Failed to insert record "+ex.getMessage());
			return populateFailureResponse("Failed to insert record"+ ex.getMessage());
		}
	}
	
	public ResponseEntity<?>  updateConversionfactor(CurrencyConversionFactor ccf){
		try {			
			CurrencyConversionFactor  currencyconversionfactor = currencyRepo.findByCurrency(ccf.getCurrency());
			if(null == currencyconversionfactor) {
				return populateFailureResponse("There is no such country code");
			}
			currencyconversionfactor.setConversionFactor(ccf.getConversionFactor());
			CurrencyConversionFactor obj = currencyRepo.save(ccf);
			log.info(" Record Updated Successfully");
			return populateSuccessResponseWithResult(obj, "Successfully Update records to database");
		} catch (Exception ex) {
		
			return populateFailureResponse("Failed to update record as no record available");
		}
	}
	
	public CurrencyConversionFactor getConversionfactor(String currency){
		
		   System.out.println(" I am calculating conversionfactor");
					
			CurrencyConversionFactor  obj = currencyRepo.findByCurrency(currency);
			/*CurrencyExchangeBean exchangeBean = new CurrencyExchangeBean();
			exchangeBean.setCountryCode(obj.getCountryCode());
			exchangeBean.setConversionFactor(obj.getConversionFactor());*/
			
			log.info(" CurrencyConversionFactor retrieved=" + obj);
			return obj;
		
	}
	
	
	
public ResponseEntity<?>   populateSuccessResponseWithResult(CurrencyConversionFactor result, String message){		
	
		respModel = new CoreResponseModel();
		respModel.setStatusCode(200);
		respModel.setMessage(message);
		respModel.setResponseBody(result);
		respEntity = new ResponseEntity<Object>(respModel,HttpStatus.OK);
		return respEntity;
	}

public ResponseEntity<?>  populateFailureResponse( String message){	
	respModel = new CoreResponseModel();
	respModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
	respModel.setMessage(message);
	respModel.setSuccess(false);		
	respEntity = new ResponseEntity<Object>(respModel,HttpStatus.BAD_REQUEST);		
	return respEntity;
}

}
