<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['id'])) {
    $id = $_REQUEST['id'];
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
mysqli_query( $con , "SET CHARACTER SET utf8;" );

$sqlquery = "UPDATE `Message` SET `is_observed`=1 WHERE `id`=$id";

if ($con->query($sqlquery) === true){
    echo "Message observed";
} else{
    echo "Error : " . $con->error;
}
