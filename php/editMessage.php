<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['content']) && isset($_REQUEST['id'])) {
    $content = $_REQUEST['content'];
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

$sqlquery = "UPDATE `Message` SET `content`='$content' WHERE `id`=$id";

if ($con->query($sqlquery) === true){
    echo "Message edited";
} else{
    echo "Error : " . $con->error;
}
