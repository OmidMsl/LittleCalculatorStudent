<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['id']) && isset($_REQUEST['name']) && isset($_REQUEST['father_name']) && isset($_REQUEST['teacher_id'])) {

    $id = $_REQUEST['id'];
    $name = $_REQUEST['name'];
    $father_name = $_REQUEST['father_name'];
    $teacher_id = $_REQUEST['teacher_id'];
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

$sqlquery = "UPDATE `Student` SET `teacher_id`=$teacher_id, `name`='$name', `father_name`='$father_name' WHERE `id`=$id";

if ($con->query($sqlquery) === true){
    echo "New record created successfully";
} else{
    echo "Error : " . $con->error;
}
