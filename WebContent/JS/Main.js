
// Create a map, centered on Davis CA by default, and register move and click listeners
initMap()
function initMap() {
	window.view = new ol.View({
		center: ol.proj.fromLonLat([-121.751392674913, 38.52247515]), // Use Davis, CA as default coordinates
		zoom: 13
	})
	window.map = new ol.Map({
		target: 'map',
		layers: [
			new ol.layer.Tile({
				source: new ol.source.OSM()
			})
		],
		view: window.view
	});
	window.map.on("movestart", onMoveStart)
	window.map.on("moveend", onMoveEnd);
	window.map.on("click", function(evt) { showUpdateEvent(evt, null); });
	window.createEvent = new ol.Overlay({
		element: document.getElementById('create-event')
	});
	window.map.addOverlay(window.createEvent);
}

function onMoveStart(e) {
	$(window.createEvent.getElement()).popover('dispose');
	$(".marker").each((ix, f) => $(f).popover('dispose'));
	$(".marker").each((ix, f) => $(f).remove());
}

// When the map moves, request event markers for new bounding box
function onMoveEnd() {
	var extent = window.map.getView().calculateExtent(window.map.getSize());
	var bottomLeft = ol.proj.toLonLat(ol.extent.getBottomLeft(extent));
	var topRight = ol.proj.toLonLat(ol.extent.getTopRight(extent));
	minLon = bottomLeft[1];
	minLat = wrapLon(bottomLeft[0]);
	maxLon = topRight[1];
	maxLat = wrapLon(topRight[0]);
	bounds = minLon + "," + minLat + "," + maxLon + "," + maxLat;
	$(".marker").each((ix, f) => $(f).remove());
	$.ajax({
		url: 'MoveServlet',
		data: {
			bounds: bounds
		},
		success: function(responseText) {
			var events = JSON.parse(responseText);
			console.log(events)
			window.events = {}
			for (var ix in events) {
				window.events[events[ix].eventID] = events[ix];
			}
			// We simply remove all overlays after index 2 (in reverse order) as only Marker overlays are added
			var overlays = window.map.getOverlays()
			for (i = overlays.array_.length - 1; i >= 2; i--) {
				window.map.removeOverlay(overlays.array_[i]);
			}
			
			$.each(window.events, function (ix, event) {
				const pos = ol.proj.fromLonLat([event.location["lon"], event.location["lat"]]);
				var markerElement = $("<div/>", {
					id: "marker-" + ix,
					class: "marker"
				});
				markerElement.bind("click", function() { showUpdateEvent(null, ix) });
				
				marker = new ol.Overlay({
					position: pos,
					positioning: "center-center",
					element: markerElement.get(0),
					stopEvent: true // We'll processing click events on the marker
				});
				window.map.addOverlay(marker);
			});
		}
	});
}

function showUpdateEvent(evt, id) {
	$(".popover").each((ix, f) => f.remove());
	if (evt != null) {
		var coordinate = evt.coordinate;
		window.createEvent.setPosition(coordinate);
		element = "#create-event";
	}
	else {
		var event = window.events[id];
		element = "#marker-" + id;
	}
	
	// Create the create/update event popover; there can be just one a time, so we use a single ID to refer to it
	$("#po").remove(); // Remove any old once
	// Create a template for the title and content; we will attach a close button and the event details to this after the fact (more reliable)
	var structure =
		'<div id="po" style="display:none" class="popover" role="tooltip">' +
			'<h3 id="ph" class="popover-header"></h3>' +
			'<div class="popover-body"></div>' +
			'<div class="arrow"></div>' +
			'<div id="popover-bottom">' +
		'</div>';
	$(element).popover({
		placement: 'top',
		html: true,
		template: structure,
		title: id == null ? "Create New Event Here" : "Edit Event",
		content: ""
	});
	$(element).popover('show'); // Produce the popover; it is still hidden
	
	// Add the close button, removing any current one first
	$("#ph > button").remove();
	$("#ph").append("<button type='button' class='close' aria-label='Close' onclick=\"$('" + element + "').popover('hide')\"><span aria-hidden='true'>&times;</span></button>")
	
	// Create the update-event element
	var update = "<div id='event-id' style='display:none'></div>" +
				"<div class='event-entry'><span>Location: </span><code class='event-val' id='event-lonlat'></code></div>" +
				"<div class='event-entry'><span>Name: </span><div class='event-val'><input type='text' class='form-control' id='event-name'></div></div>" +
				"<div class='event-entry'><span>Time: </span><div class='event-val'>" +
					"<input type='datetime-local' step=1800 class='form-control' id='event-time'/>" +
				"</div>" +
				"<div class='event-entry'><span>Attendees: </span><div class='event-val' id='event-attendees'></div></div>";
	$("#popover-bottom").html(update)
	if (id != null) {
		$("#event-id").html(id)
		var event = window.events[id];
		console.log(event)
		var lonLat = [event.location["lon"], event.location["lat"]];
		$("#event-lonlat").html(lonLat[0].toFixed(5) + ", " + lonLat[1].toFixed(5));
		$("#event-name").prop("value", event.name);
		$("#event-name").prop("readonly", true);
		$("#event-time").prop("value", event.date);
		$("#event-time").prop("readonly", true);
		$.each(event.students, (ix, student) => $("#event-attendees").append("<div class='event-attendee'>" + student.firstName + " " + student.lastName + "</div>"));
		if (event.students.length < 5)
			$("#event-attendees").append("<input class='form-control event-val' id='event-add-student' type='text' placeholder='Add student' />")
	}
	else {
		var coordinate = evt.coordinate;
		var lonLat = ol.proj.toLonLat(coordinate);
		$("#event-lonlat").html(lonLat[0].toFixed(5) + ", " + lonLat[1].toFixed(5));
		$("#event-attendees").append("<input class='form-control event-val' id='event-add-student' type='text' placeholder='Add student' />")
	}
	$("#popover-bottom").append("<button id='submit-button' class='btn btn-dark' onclick='updateEvent()'>Submit</button>")
	$("#po").show();
}

function updateEvent() {
	var id = $("#event-id").html();
	if (id.length == 0) id = "0"; // Signals that this is a new event
	var lonlat = $("#event-lonlat").html();
	var name = $("#event-name").prop("value");
	var date = $("#event-time").prop("value");
	var attendees = []
	$(".event-attendee").each((i, att) => attendees.push($(att).html()));
	var newAttendee = $("#event-add-student").prop("value");
	if (newAttendee.length > 0) attendees.push(newAttendee);
	if (id === "0" && (name.length == 0 || date.length == 0 || attendees.length == 0)) {
		alert("Could not create event; provide at least a title, date and one attendee")
		return
	}
	$.ajax({
		url: 'UpdateServlet',
		data: {
			eventId: id,
			eventLoc: lonlat,
			eventName: name,
			eventDate: date,
			eventAttendees: attendees
		},
		success: function(responseText) {
			if (responseText !== "Success!") {
				alert("Could not " + (id === "0" ? "create" : "update") + " event: " + responseText)
			}
			else {
				$(".popover").each((ix, f) => f.remove());
				onMoveEnd();				
			}
		}
	});
}

// On search, query for location and move map accordingly
$('#searchform').submit(function(event){
	event.preventDefault();
	var place = document.getElementById("place-input").value
	console.log(place);
	$.ajax({
		url: 'SearchServlet',
		data: {
			place: place
		},
		success: function(responseText) {
			var parts = responseText.split("\t");
			var lon = parseFloat(parts[0]);
			var lat = parseFloat(parts[1]);
			var bounds = JSON.parse(parts[2]);
			gotoLoc(lon, lat, bounds);
		}
	});
});

/*
 * Some util functions below here. lat/lon functions based on based on https://openlayers.org/en/latest/examples/moveend.html
 */
function gotoLoc(lon, lat, bounds) {
	const coordinates = ol.proj.fromLonLat([lon, lat]);
	window.view.animate({center: coordinates, zoom: 13});
	if (bounds) {
		window.view.fit(ol.proj.transformExtent(bounds, 'EPSG:4326', 'EPSG:3857'), window.map.getSize())
	}
}

function wrapLon(value) {
	var worlds = Math.floor((value + 180) / 360);
	return value - (worlds * 360);
}

