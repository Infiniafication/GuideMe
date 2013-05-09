<?php
 
$response = array();
 
if (isset($_POST['pid']) && isset($_POST['name']) && isset($_POST['price']) && isset($_POST['description'])) {
 
    $pid = $_POST['pid'];
    $name = $_POST['name'];
    $price = $_POST['price'];
    $description = $_POST['description'];
 
    require_once __DIR__ . '/db_config.php';
 
    $db = new DB_CONNECT();
    //Updating mysql
    $result = mysql_query("UPDATE products SET name = '$name', price = '$price', description = '$description' WHERE pid = $pid");
 
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
 
        echo json_encode($response);
    } else {
 
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>