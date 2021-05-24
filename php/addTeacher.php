<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['name']) && isset($_REQUEST['city']) && isset($_REQUEST['school'])) {
    if ($_REQUEST['id'] === "") {
        $id = "NULL";
    } else {
        $id = $_REQUEST['id'];
    }
    $name = $_REQUEST['name'];
    $city = $_REQUEST['city'];
    $school = $_REQUEST['school'];
} else{
    return;
}

// connect to database
$con = new mysqli(DB_HOST , DB_USER , DB_PASS , DB_NAME);

// check connection
if ($con->connect_error) {
    echo "Failed to connect : " . $con->connect_error;
    return;
}

$sqlquery = "INSERT INTO `Teacher` (`id`, `name`, `city`, `school`) VALUES ($id, '$name', '$city', '$school')";

if ($con->query($sqlquery) === true){
    echo "New record created successfully";
} else{
    echo "Error : " . $con->error;
}
