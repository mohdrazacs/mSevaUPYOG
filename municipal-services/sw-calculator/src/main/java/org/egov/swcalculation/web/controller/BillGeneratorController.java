package org.egov.swcalculation.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.swcalculation.repository.SewerageCalculatorDao;
import org.egov.swcalculation.service.BillGeneratorService;
import org.egov.swcalculation.util.ResponseInfoFactory;
import org.egov.swcalculation.validator.BillGenerationValidator;
import org.egov.swcalculation.web.models.BillGenerationRequest;
import org.egov.swcalculation.web.models.BillGenerationSearchCriteria;
import org.egov.swcalculation.web.models.BillScheduler;
import org.egov.swcalculation.web.models.BillSchedulerResponse;
import org.egov.swcalculation.web.models.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@RestController
@RequestMapping("/seweragecharges")
@Slf4j
public class BillGeneratorController {

	@Autowired
	private final ResponseInfoFactory responseInfoFactory;

	@Autowired
	private BillGeneratorService billGeneratorService;

	@Autowired
	private BillGenerationValidator billGenerationValidator;

	@Autowired
	private SewerageCalculatorDao sewerageCalculatorDao;

	@PostMapping("/scheduler/_create")
	public ResponseEntity<BillSchedulerResponse> billSchedulerCreate(
			@Valid @RequestBody BillGenerationRequest billGenerationReq) {

		BillSchedulerResponse response = new BillSchedulerResponse();
		 response = BillSchedulerResponse.builder().billSchedulers( billGeneratorService.bulkbillgeneration(billGenerationReq) )
					.responseInfo(
							responseInfoFactory.createResponseInfoFromRequestInfo(billGenerationReq.getRequestInfo(), true))
					.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@PostMapping("/scheduler/_search")
	public ResponseEntity<BillSchedulerResponse> billSchedulerSearch(
			@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute BillGenerationSearchCriteria criteria) {
		List<BillScheduler> billSchedulers = billGeneratorService.getBillGenerationDetails(criteria);
		BillSchedulerResponse response = BillSchedulerResponse.builder().billSchedulers(billSchedulers).responseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
