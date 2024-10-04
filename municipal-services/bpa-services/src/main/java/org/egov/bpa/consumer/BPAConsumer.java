package org.egov.bpa.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.egov.bpa.web.model.Workflow;

import org.egov.bpa.service.BPAService;
import org.egov.bpa.service.notification.BPANotificationService;
import org.egov.bpa.web.model.BPA;
import org.egov.bpa.web.model.BPAEscalationRequest;
import org.egov.bpa.util.BPAConstants;
import org.egov.bpa.web.model.BPARequest;
import org.egov.bpa.web.model.BPASearchCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BPAConsumer {

	@Autowired
	private BPANotificationService notificationService;
	
	@Autowired
	private BPAService bpaService;
	
	@KafkaListener(topics = { "${persister.update.buildingplan.topic}", "${persister.save.buildingplan.topic}",
			"${persister.update.buildingplan.workflow.topic}" })
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		BPARequest bpaRequest = new BPARequest();
		try {
			log.debug("Consuming record: " + record);
			bpaRequest = mapper.convertValue(record, BPARequest.class);
		} catch (final Exception e) {
			log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
		}
		log.debug("BPA Received: " + bpaRequest.getBPA().getApplicationNo());
		notificationService.process(bpaRequest);
	}
	
	@KafkaListener(topics = { "${persister.create.escalation.demand.topic}"})
	public void generateEscalationDemand(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		BPAEscalationRequest processInstanceRequest = new BPAEscalationRequest();
		List<BPA> bpas=new ArrayList<BPA>();
		RequestInfo requestInfo=new RequestInfo();
       		Workflow workflow = new Workflow();
			try {
			log.info("Consuming record: " + record);
			processInstanceRequest = mapper.convertValue(record, BPAEscalationRequest.class);
			requestInfo.setAuthToken(processInstanceRequest.getBPAEscalationInstances().get(0).getAuthToken());
			requestInfo.setUserInfo(processInstanceRequest.getBPAEscalationInstances().get(0).getUserInfo());

			BPASearchCriteria criteria = new BPASearchCriteria();
			criteria.setTenantId(processInstanceRequest.getBPAEscalationInstances().get(0).getTenantId());
			criteria.setApplicationNo(processInstanceRequest.getBPAEscalationInstances().get(0).getBusinessId());
			bpas = bpaService.search(criteria, requestInfo);
			log.info("exiting Search function2");

			log.debug("BPA Received: " + processInstanceRequest.getBPAEscalationInstances().get(0).getBusinessId());

		} catch (final Exception e) {
			log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
		}
		log.debug("BPA Received: " + processInstanceRequest.getBPAEscalationInstances().get(0).getBusinessId());

        if (!bpas.isEmpty()) {
        	log.info("inside update");
        	workflow.setAssignes(null);
            	workflow.setAction(BPAConstants.ACTION_APPROVE); 
            	workflow.setVarificationDocuments(null);
        	BPARequest bpaRequest=new BPARequest().BPA(bpas.get(0)).requestInfo(requestInfo);
        	bpas.get(0).setWorkflow(workflow);
		log.info("Proceeding for Update call");

        	//bpasearch (response)  -> Approve bpa request
		log.info("Proceeding for Update call2");
            	bpaService.update(bpaRequest);
	}
}
}
