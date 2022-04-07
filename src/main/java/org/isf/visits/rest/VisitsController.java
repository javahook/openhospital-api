/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2021 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.isf.visits.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.isf.shared.exceptions.OHAPIException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.visits.dto.VisitDTO;
import org.isf.visits.manager.VisitManager;
import org.isf.visits.mapper.VisitMapper;
import org.isf.visits.model.Visit;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;

@RestController
@Api(value = "/visit", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitsController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(VisitsController.class);

    @Autowired
    protected VisitManager visitManager;

	@Autowired
	private WardBrowserManager wardManager;
    
    @Autowired
    protected VisitMapper mapper;

    public VisitsController(VisitManager visitManager, VisitMapper visitMapper) {
        this.visitManager = visitManager;
        this.mapper = visitMapper;
    }

    /**
     * Get all the visitors related to a patient.
     *
     * @param patID the id of the patient
     * @return NO_CONTENT if there aren't visitors, {@code List<VaccineDTO>} otherwise
     * @throws OHServiceException
     */
    @GetMapping(value = "/visit/{patID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VisitDTO>> getVisit(@PathVariable int patID) throws OHServiceException {
        LOGGER.info("Get visit related to patId: {}", patID);
        List<Visit> visit = visitManager.getVisits(patID);
        List<VisitDTO> listVisit = mapper.map2DTOList(visit);
        if (listVisit.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.ok(listVisit);
        }
    }

    /**
     * Create a new visitor.
     *
     * @param newVisit
     * @return an error if there are some problem, the visitor id (Integer) otherwise
     * @throws OHServiceException
     */
    @PostMapping(value = "/visit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> newVisit(@RequestBody VisitDTO newVisit) throws OHServiceException {
	    LOGGER.info("Create Visit: {}", newVisit);
		Visit visit = mapper.map2Model(newVisit);

		if (newVisit.getWard() != null && newVisit.getWard().getCode() != null && !newVisit.getWard().getCode().trim().isEmpty()) {
			Ward ward = wardManager.findWard(newVisit.getWard().getCode());
			if (ward == null) {
				throw new OHAPIException(new OHExceptionMessage(null, "Ward not found!", OHSeverityLevel.ERROR));
			}
			visit.setWard(ward);
		} else {
			throw new OHAPIException(new OHExceptionMessage(null, "Ward field is required!", OHSeverityLevel.ERROR));
		}
		visit = visitManager.saveVisit(visit);
        return ResponseEntity.status(HttpStatus.CREATED).body(visit.getVisitID()); //TODO: verify if it's correct
    }

    /**
     * Create new visitors.
     *
     * @param newVisitsDTO a list with all the visitors
     * @return an error message if there are some problem, ok otherwise
     * @throws OHServiceException
     */
    @PostMapping(value = "/visits", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity newVisits(@RequestBody List<VisitDTO> newVisitsDTO) throws OHServiceException {
        LOGGER.info("Create Visits");
        List<Visit> newVisits = new ArrayList<>();

		for (VisitDTO visitDTO: newVisitsDTO) {
			Visit visit = mapper.map2Model(visitDTO);
			if (visitDTO.getWard() != null && visitDTO.getWard().getCode() != null && !visitDTO.getWard().getCode().trim().isEmpty()) {
				Ward ward = wardManager.findWard(visitDTO.getWard().getCode());
				if (ward == null) {
					throw new OHAPIException(new OHExceptionMessage(null, "Ward not found!", OHSeverityLevel.ERROR));
				}
				visit.setWard(ward);
				newVisits.add(visit);
			} else {
				throw new OHAPIException(new OHExceptionMessage(null, "Ward field is required!", OHSeverityLevel.ERROR));
			}
		}
        boolean areCreated = visitManager.newVisits(newVisits);
        if (!areCreated) {
            throw new OHAPIException(new OHExceptionMessage(null, "Visits are not created!", OHSeverityLevel.ERROR));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(areCreated);
    }

	/**
	 * Update a visitor.
	 *
	 * @param visitID
	 * @param updateVisit
	 * @return an error if there are some problem, the visitor id (Integer) otherwise
	 * @throws OHServiceException
	 */
	@PutMapping(value = "/visit/{visitID}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Integer> updatePatient(@PathVariable int visitID, @RequestBody VisitDTO updateVisit) throws OHServiceException {
		LOGGER.info("Update patient id: {}", visitID);
		Visit updateVisitModel = mapper.map2Model(updateVisit);
		updateVisitModel.setVisitID(visitID);
		if (updateVisit.getWard() != null && updateVisit.getWard().getCode() != null && !updateVisit.getWard().getCode().trim().isEmpty()) {
			Ward ward = wardManager.findWard(updateVisit.getWard().getCode());
			if (ward == null) {
				throw new OHAPIException(new OHExceptionMessage(null, "Ward not found!", OHSeverityLevel.ERROR));
			}
			updateVisitModel.setWard(ward);
		} else {
			throw new OHAPIException(new OHExceptionMessage(null, "Ward field is required!", OHSeverityLevel.ERROR));
		}
		 updateVisitModel = visitManager.saveVisit(updateVisitModel);
		if (updateVisitModel == null) {
			throw new OHAPIException(new OHExceptionMessage(null, "Visit is not updated!", OHSeverityLevel.ERROR));
		}
		return ResponseEntity.ok(updateVisitModel.getVisitID());
	}

    /**
     * Delete all the visits related to a patient.
     *
     * @param patID the id of the patient
     * @return an error message if there are some problem, ok otherwise
     * @throws OHServiceException
     */
    @DeleteMapping(value = "/visit/{patID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteVisitsRelatedToPatient(@PathVariable int patID) throws OHServiceException {
	    LOGGER.info("Delete Visit related to patId: {}", patID);
        boolean areDeleted = visitManager.deleteAllVisits(patID);
        if (!areDeleted) {
            throw new OHAPIException(new OHExceptionMessage(null, "Visits are not deleted!", OHSeverityLevel.ERROR));
        }
        return ResponseEntity.ok(true);
    }

}
