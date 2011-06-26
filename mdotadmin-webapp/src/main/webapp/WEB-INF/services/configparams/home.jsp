<%--
  Copyright 2011 The Kuali Foundation Licensed under the Educational Community
  License, Version 2.0 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at
  http://www.osedu.org/licenses/ECL-2.0 Unless required by applicable law or
  agreed to in writing, software distributed under the License is distributed
  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  express or implied. See the License for the specific language governing
  permissions and limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Configuration Parameters</title>
	<link type="text/css" href="/admin/css/ui-lightness/jquery-ui-1.8.13.custom.css" rel="stylesheet" />	
	<script type="text/javascript" src="/admin/js/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="/admin/js/jquery-ui-1.8.13.custom.min.js"></script>
	<script type="text/javascript" src="/admin/js/jquery-jtemplates.js"></script>
	<script type="text/javascript">
	jQuery.fn.serializeObject = function() {
		  var arrayData, objectData;
		  arrayData = this.serializeArray();
		  objectData = {};

		  $.each(arrayData, function() {
		    var value;

		    if (this.value != null) {
		      value = this.value;
		    } else {
		      value = '';
		    }

		    if (objectData[this.name] != null) {
		      if (!objectData[this.name].push) {
		        objectData[this.name] = [objectData[this.name]];
		      }

		      objectData[this.name].push(value);
		    } else {
		      objectData[this.name] = value;
		    }
		  });

		  return objectData;
		};

	
	$(function() {
		var name = $("#cpname1");
		var value = $("#cpvalue1");
		
		// Button create-new functionality
		$( "#create-new" )
		.button()
		.click(function() {
			$( "#dialog-form" ).dialog( "open" );
		});
		
		// Form create-new configuration
		$( "#dialog-form" ).dialog({
			autoOpen: false,
			height: 400,
			width: 500,
			modal: true,
			buttons: {
				"Save": function() {
					var bValid = true;
					//allFields.removeClass( "ui-state-error" );

					// Ajax call here
 					$.ajax({
					    type: "POST",
					    url: "configparams",
					    //data: "{'PageSize':'" + name.val() + "', 'Page':'" + value.val() + "'}",
					    //$( "#form-new" ).serializeArray()
					    data: JSON.stringify($( "#form-new" ).serializeObject()),
					    contentType: "application/json; charset=utf-8",
					    //contentType: "text/plain; charset=utf-8",
					    dataType: "json",
					    success: function(msg) {
					      // Render the resulting data, via template.
					    	displayTable(1);
					    }
					});
					//alert($( "#form-new" ).serializeObject());
					$("#cpname1").val('');
					$("#cpvalue1").val('');
					$( this ).dialog( "close" );
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				//allFields.val( "" ).removeClass( "ui-state-error" );
			}
		});
		
		// Form delete configuration
		$("#dialog-form-delete").dialog({
		    autoOpen: false,
		    resizable: false,
		    height:300,
			width: 350,
		    modal: true,
		    buttons: {
		        'Delete': function() {
		            $(this).dialog('close');
		            //var path = $(this).data('link').href;
		            //$(location).attr('href', path);
 					$.ajax({
					    type: "DELETE",
					    url: $(this).data('link').href,
					    contentType: "application/json; charset=utf-8",
					    dataType: "json",
					    success: function(msg) {
							displayTable(1);
					    }
					});
		        },
		        Cancel: function() {
		            $(this).dialog('close');
		        }
		    },
		    open: function(event, ui) {
				$.ajax({
				    type: "GET",
				    url: "configparams/" + $(this).data('link').name,
				    contentType: "application/json; charset=utf-8",
				    dataType: "json",
				    success: function(msg) {
				    	//$("#deleteConfigParamId").html(JSON.stringify(msg));
				    	applyDataTemplate(msg);
				    }
				});
		    	//$("#deleteConfigParamId").html($(this).data('link').name);
		    }
		});
		
		$("#dialog-form-edit").dialog({autoOpen: false});
		
	}); // function
	
	var pageSize = 5;
	 
	$(document).ready(function() {
	  // Display the first page of results initially.
	  displayTable(1);
	});
	 
	function displayTable(page) {
	  $.ajax({
	    type: "GET",
	    url: "configparams",
	    //data: "{'PageSize':'" + pageSize + "', 'Page':'" + page + "'}",
	    data: "t=1",
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    success: function(msg) {
	      // Render the resulting data, via template.
	      applyTemplate(msg);
	    }
	  });
	}
	 
	function applyTemplate(msg) {
	  $('#container').setTemplate($("#TemplateResultsTable").html());
	  $('#container').processTemplate(msg);
/* 		 $("#container").setTemplate("Template by {$T.bold()} version <em>{$Q.version}</em>.");
		 $("#container").processTemplate("jTemplates"); */
		bind();
	}
	
	function applyDataTemplate(msg) {
		  $('#deleteConfigParamId').setTemplate($("#TemplateResultsTable2").html());
		  $('#deleteConfigParamId').processTemplate(msg);
		}
	
	function bind() {
		  $('a[cmdtype*=delete]').bind('click', function(e) {
			    e.preventDefault();
			    $("#dialog-form-delete")
			        .data('link', this)
			        .dialog('open');
			    return false;
			});
		  $('a[cmdtype*=edit]').bind('click', function(e) {
			    e.preventDefault();
				$.ajax({
				    type: "GET",
				    url: "configparams/" + this.name,
				    contentType: "application/json; charset=utf-8",
				    dataType: "json",
				    success: function(msg) {
				    	$("#cpid").val(msg.configParamId);
				    	$("#cpversionnumber").val(msg.versionNumber);
				    	$("#cpname").val(msg.name);
				    	$("#cpvalue").val(msg.value);
						// Form edit configuration
						$("#dialog-form-edit").dialog({
						    autoOpen: false,
						    resizable: false,
						    height:400,
							width: 500,
						    modal: true,
						    buttons: {
						        'Save': function() {
				 					$.ajax({
									    type: "PUT",
									    url: "configparams",
									    data: JSON.stringify($( "#form-edit" ).serializeObject()),
									    contentType: "application/json; charset=utf-8",
									    dataType: "json",
									    success: function(msg) {
									    	displayTable(1);
									    }
									});
									$( this ).dialog( "close" );
						        },
						        Cancel: function() {
						            $(this).dialog('close');
						        }
						    },
						    open: function(event, ui) {

						    	//$("#deleteConfigParamId").html($(this).data('link').name);
						    }
						});

					    $("#dialog-form-edit")
				        .data('link', this)
				        .dialog('open');
				    }
				});
			    return false;
			});
	} // bind
	</script>

</head>
<body>
<h2><a href="/admin">Admin Home</a> / Configuration Parameters</h2>
<button aria-disabled="false" role="button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="create-new"><span class="ui-button-text">Create new config param</span></button>

<div id="container" class="ui-widget"></div>

<div id="dialog-form" title="Create new configuration parameter">
	<p class="validateTips">All form fields are required.</p>

	<form id="form-new">
	<fieldset>
		<label for="name">Name</label>
		<input type="text" name="name" id="cpname1" class="text ui-widget-content ui-corner-all" />
		<br/>
		<label for="value">Value:</label>
		<textarea name="value" id="cpvalue1" rows="2" cols="50" class="text ui-widget-content ui-corner-all"></textarea>
	</fieldset>
	</form>
</div>

<div id="dialog-form-delete" title="Delete configuration parameter">
	<p class="validateTips">Are you sure you want to delete this?</p>
	<div id="deleteConfigParamId"></div>
</div>

<div id="dialog-form-edit" title="Edit configuration parameter">
	<p class="validateTips">All form fields are required.</p>

	<form id="form-edit">
	<fieldset>
		<input type="hidden" name="configParamId" id="cpid" />
		<input type="hidden" name="versionNumber" id="cpversionnumber" />
		<label for="name">Name:</label>
		<input type="text" name="name" id="cpname" class="text ui-widget-content ui-corner-all" />
		<br/>
		<label for="value">Value:</label>
		<textarea name="value" id="cpvalue" rows="2" cols="50" class="text ui-widget-content ui-corner-all"></textarea>
	</fieldset>
	</form>
</div>
	
<%-- Results Table Template --%>
<script type="text/html" id="TemplateResultsTable">
{#template MAIN}
<table class="ui-widget ui-widget-content">
  <tr class="ui-widget-header">
    <th>Name</th>
    <th>Value</th>
	<th>Action</th>
    <th>Action</th>
  </tr>
  {#foreach $T as r}
    {#include ROW root=$T.r}
  {#/for}
</table>
{#/template MAIN}

{#template ROW}
<tr class="{#cycle values=['','evenRow']}">
  <td>{$T.name}</td>
  <td>{$T.value}</td>
  <td>
    <a href="configparams/{$T.configParamId}" name="{$T.configParamId}" cmdtype="delete">Delete</a>
  </td>
  <td>
    <a href="configparams/{$T.configParamId}" name="{$T.configParamId}" cmdtype="edit">Edit</a>
  </td>
</tr>
{#/template ROW}
</script>

<%-- Data No Action Table Template --%>
<script type="text/html" id="TemplateResultsTable2">
{#template MAIN}
<table class="ui-widget ui-widget-content">
<tr>
  <td valign="top">Name:</td>
  <td>{$T.name}</td>
</tr>
<tr>
  <td valign="top">Value:</td>
  <td>{$T.value}</td>
</tr>
</table>
{#/template MAIN}
</script>

</body>
</html>
