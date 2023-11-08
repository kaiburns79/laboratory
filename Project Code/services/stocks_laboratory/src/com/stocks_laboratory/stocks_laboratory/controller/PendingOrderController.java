/*Copyright (c) 2023-2024 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.stocks_laboratory.stocks_laboratory.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wavemaker.commons.wrapper.StringWrapper;
import com.wavemaker.runtime.commons.file.manager.ExportedFileManager;
import com.wavemaker.runtime.commons.file.model.Downloadable;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.tools.api.core.annotations.MapTo;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.stocks_laboratory.stocks_laboratory.PendingOrder;
import com.stocks_laboratory.stocks_laboratory.service.PendingOrderService;


/**
 * Controller object for domain model class PendingOrder.
 * @see PendingOrder
 */
@RestController("stocks_laboratory.PendingOrderController")
@Api(value = "PendingOrderController", description = "Exposes APIs to work with PendingOrder resource.")
@RequestMapping("/stocks_laboratory/PendingOrder")
public class PendingOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PendingOrderController.class);

    @Autowired
	@Qualifier("stocks_laboratory.PendingOrderService")
	private PendingOrderService pendingOrderService;

	@Autowired
	private ExportedFileManager exportedFileManager;

	@ApiOperation(value = "Creates a new PendingOrder instance.")
    @PostMapping
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public PendingOrder createPendingOrder(@RequestBody PendingOrder pendingOrder) {
		LOGGER.debug("Create PendingOrder with information: {}" , pendingOrder);

		pendingOrder = pendingOrderService.create(pendingOrder);
		LOGGER.debug("Created PendingOrder with information: {}" , pendingOrder);

	    return pendingOrder;
	}

    @ApiOperation(value = "Returns the PendingOrder instance associated with the given id.")
    @GetMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public PendingOrder getPendingOrder(@PathVariable("id") Integer id) {
        LOGGER.debug("Getting PendingOrder with id: {}" , id);

        PendingOrder foundPendingOrder = pendingOrderService.getById(id);
        LOGGER.debug("PendingOrder details with id: {}" , foundPendingOrder);

        return foundPendingOrder;
    }

    @ApiOperation(value = "Updates the PendingOrder instance associated with the given id.")
    @PutMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public PendingOrder editPendingOrder(@PathVariable("id") Integer id, @RequestBody PendingOrder pendingOrder) {
        LOGGER.debug("Editing PendingOrder with id: {}" , pendingOrder.getId());

        pendingOrder.setId(id);
        pendingOrder = pendingOrderService.update(pendingOrder);
        LOGGER.debug("PendingOrder details with id: {}" , pendingOrder);

        return pendingOrder;
    }
    
    @ApiOperation(value = "Partially updates the PendingOrder instance associated with the given id.")
    @PatchMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public PendingOrder patchPendingOrder(@PathVariable("id") Integer id, @RequestBody @MapTo(PendingOrder.class) Map<String, Object> pendingOrderPatch) {
        LOGGER.debug("Partially updating PendingOrder with id: {}" , id);

        PendingOrder pendingOrder = pendingOrderService.partialUpdate(id, pendingOrderPatch);
        LOGGER.debug("PendingOrder details after partial update: {}" , pendingOrder);

        return pendingOrder;
    }

    @ApiOperation(value = "Deletes the PendingOrder instance associated with the given id.")
    @DeleteMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deletePendingOrder(@PathVariable("id") Integer id) {
        LOGGER.debug("Deleting PendingOrder with id: {}" , id);

        PendingOrder deletedPendingOrder = pendingOrderService.delete(id);

        return deletedPendingOrder != null;
    }

    /**
     * @deprecated Use {@link #findPendingOrders(String, Pageable)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of PendingOrder instances matching the search criteria.")
    @PostMapping(value = "/search")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<PendingOrder> searchPendingOrdersByQueryFilters( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering PendingOrders list by query filter:{}", (Object) queryFilters);
        return pendingOrderService.findAll(queryFilters, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of PendingOrder instances matching the optional query (q) request param. If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, sort can be sent as request parameters. The sort value should be a comma separated list of field names & optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @GetMapping
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<PendingOrder> findPendingOrders(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering PendingOrders list by filter:", query);
        return pendingOrderService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of PendingOrder instances matching the optional query (q) request param. This API should be used only if the query string is too big to fit in GET request with request param. The request has to made in application/x-www-form-urlencoded format.")
    @PostMapping(value="/filter", consumes= "application/x-www-form-urlencoded")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<PendingOrder> filterPendingOrders(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering PendingOrders list by filter", query);
        return pendingOrderService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param.")
    @GetMapping(value = "/export/{exportType}", produces = "application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Downloadable exportPendingOrders(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return pendingOrderService.export(exportType, query, pageable);
    }

    @ApiOperation(value = "Returns a URL to download a file for the data matching the optional query (q) request param and the required fields provided in the Export Options.") 
    @PostMapping(value = "/export", consumes = "application/json")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public StringWrapper exportPendingOrdersAndGetURL(@RequestBody DataExportOptions exportOptions, Pageable pageable) {
        String exportedFileName = exportOptions.getFileName();
        if(exportedFileName == null || exportedFileName.isEmpty()) {
            exportedFileName = PendingOrder.class.getSimpleName();
        }
        exportedFileName += exportOptions.getExportType().getExtension();
        String exportedUrl = exportedFileManager.registerAndGetURL(exportedFileName, outputStream -> pendingOrderService.export(exportOptions, pageable, outputStream));
        return new StringWrapper(exportedUrl);
    }

	@ApiOperation(value = "Returns the total count of PendingOrder instances matching the optional query (q) request param.")
	@GetMapping(value = "/count")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Long countPendingOrders( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting PendingOrders");
		return pendingOrderService.count(query);
	}

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@PostMapping(value = "/aggregations")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Page<Map<String, Object>> getPendingOrderAggregatedValues(@RequestBody AggregationInfo aggregationInfo, Pageable pageable) {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return pendingOrderService.getAggregatedValues(aggregationInfo, pageable);
    }


    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service PendingOrderService instance
	 */
	protected void setPendingOrderService(PendingOrderService service) {
		this.pendingOrderService = service;
	}

}