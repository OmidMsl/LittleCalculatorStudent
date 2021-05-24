<?php

require_once 'config.php';
// get parameters
if (isset($_REQUEST['teacher_id'])){
    $tid = $_REQUEST['teacher_id'];
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

$content = mysqli_query($con , "SELECT * FROM `Student` WHERE `teacher_id`=$tid");
$output = array();

while($row = mysqli_fetch_array($content)){
    $record  = array();
    $record['id'] = $row['id'];
    $record['name'] = $row['name'];
    $record['father_name'] = $row['father_name'];
    $output[] = $record;
}

echo json_encode($output);
