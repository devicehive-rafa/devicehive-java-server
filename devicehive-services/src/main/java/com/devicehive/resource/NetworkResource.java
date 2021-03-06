package com.devicehive.resource;

import com.devicehive.model.updates.NetworkUpdate;
import com.devicehive.vo.NetworkVO;
import com.devicehive.vo.NetworkWithUsersAndDevicesVO;
import io.swagger.annotations.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(tags = {"Network"}, value = "Represents a network, an isolated area where devices reside.", consumes="application/json")
@Path("/network")
public interface NetworkResource {

    /**
     * Produces following output:
     * <pre>
     * [
     *  {
     *    "description":"Network Description",
     *    "id":1,
     *    "key":"Network Key",
     *    "name":"Network Name"
     *   },
     *   {
     *    "description":"Network Description",
     *    "id":2,
     *    "key":"Network Key",
     *    "name":"Network Name"
     *   }
     * ]
     * </pre>
     *
     * @param name        exact network's name, ignored, when  namePattern is not null
     * @param namePattern name pattern
     * @param sortField   Sort Field, can be either "id", "key", "name" or "description"
     * @param sortOrderSt ASC - ascending, otherwise descending
     * @param take        limit, default 1000
     * @param skip        offset, default 0
     */
    @GET
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN', 'KEY') and hasPermission(null, 'GET_NETWORK')")
    @ApiOperation(value = "List networks", notes = "Gets list of device networks the client has access to.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "If successful, this method returns array of Network resources in the response body.", response = NetworkVO.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "If request parameters invalid"),
            @ApiResponse(code = 401, message = "If request is not authorized"),
            @ApiResponse(code = 403, message = "If principal doesn't have permissions")
    })
    Response list(
            @ApiParam(name = "name", value = "Filter by network name.")
            @QueryParam("name")
            String name,
            @ApiParam(name = "namePattern", value = "Filter by network name pattern.")
            @QueryParam("namePattern")
            String namePattern,
            @ApiParam(name = "sortField", value = "Result list sort field.", allowableValues = "ID,Name")
            @QueryParam("sortField")
            String sortField,
            @ApiParam(name = "sortOrder", value = "Result list sort order.", allowableValues = "ASC,DESC")
            @QueryParam("sortOrder")
            String sortOrderSt,
            @ApiParam(name = "take", value = "Number of records to take from the result list.", defaultValue = "20")
            @QueryParam("take")
            @Min(0) @Max(Integer.MAX_VALUE)
            Integer take,
            @ApiParam(name = "skip", value = "Number of records to skip from the result list.", defaultValue = "0")
            @QueryParam("skip")
            Integer skip
    );

    /**
     * Generates  JSON similar to this:
     * <pre>
     *     {
     *      "description":"Network Description",
     *      "id":1,
     *      "key":"Network Key",
     *      "name":"Network Name"
     *     }
     * </pre>
     *
     * @param id network id, can't be null
     */
    @GET
    @Path("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN', 'KEY') and hasPermission(null, 'GET_NETWORK')")
    @ApiOperation(value = "Get network", notes = "Gets information about device network and its devices.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "If successful, this method returns a Network resource in the response body.", response = NetworkVO.class),
            @ApiResponse(code = 400, message = "If request is malformed"),
            @ApiResponse(code = 401, message = "If request is not authorized"),
            @ApiResponse(code = 403, message = "If principal doesn't have permissions"),
            @ApiResponse(code = 404, message = "If network not found")
    })
    Response get(
            @ApiParam(name = "id", value = "Network identifier.")
            @PathParam("id")
            long id);

    /**
     * Inserts new Network into database. Consumes next input:
     * <pre>
     *     {
     *       "key":"Network Key",
     *       "name":"Network Name",
     *       "description":"Network Description"
     *     }
     * </pre>
     * Where "key" is not required "description" is not required "name" is required <p/> In case of success will produce
     * following output:
     * <pre>
     *     {
     *      "description":"Network Description",
     *      "id":1,
     *      "key":"Network Key",
     *      "name":"Network Name"
     *     }
     * </pre>
     * Where "description" and "key" will be provided, if they are specified in request. Fields "id" and "name" will be
     * provided anyway.
     */
    @POST
    @PreAuthorize("hasAnyRole('ADMIN', 'KEY') and hasPermission(null, 'MANAGE_NETWORK')")
    @ApiOperation(value = "Create network", notes = "Creates new device network.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "If successful, this method returns a Network resource in the response body.", response = NetworkVO.class),
            @ApiResponse(code = 400, message = "If request is malformed"),
            @ApiResponse(code = 401, message = "If request is not authorized"),
            @ApiResponse(code = 403, message = "If principal doesn't have permissions")
    })
    Response insert(
            @ApiParam(value = "Network body", defaultValue = "{}", required = true)
            NetworkVO network);

    /**
     * This method updates network with given Id. Consumes following input:
     * <pre>
     *     {
     *       "key":"Network Key",
     *       "name":"Network Name",
     *       "description":"Network Description"
     *     }
     * </pre>
     * Where "key" is not required "description" is not required "name" is not required Fields, that are not specified
     * will stay unchanged Method will produce following output:
     * <pre>
     *     {
     *      "description":"Network Description",
     *      "id":1,
     *      "key":"Network Key",
     *      "name":"Network Name"
     *     }
     * </pre>
     */
    @PUT
    @Path("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'KEY') and hasPermission(null, 'MANAGE_NETWORK')")
    @ApiOperation(value = "Update network", notes = "Updates an existing device network.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "If successful, this method returns an empty response body."),
            @ApiResponse(code = 400, message = "If request is malformed"),
            @ApiResponse(code = 401, message = "If request is not authorized"),
            @ApiResponse(code = 403, message = "If principal doesn't have permissions")
    })
    Response update(
            @ApiParam(value = "Network body", defaultValue = "{}", required = true)
            NetworkUpdate networkToUpdate,
            @ApiParam(name = "id", value = "Network identifier.", required = true)
            @PathParam("id")
            long id);

    /**
     * Deletes network by specified id. If success, outputs empty response
     *
     * @param id network's id
     */
    @DELETE
    @Path("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'KEY') and hasPermission(null, 'MANAGE_NETWORK')")
    @ApiOperation(value = "Delete network", notes = "Deletes an existing device network.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "If successful, this method returns an empty response body."),
            @ApiResponse(code = 401, message = "If request is not authorized"),
            @ApiResponse(code = 403, message = "If principal doesn't have permissions"),
            @ApiResponse(code = 404, message = "If network not found")
    })
    Response delete(
            @ApiParam(name = "id", value = "Network identifier.", required = true)
            @PathParam("id")
            long id);

}
