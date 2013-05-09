<?php 
	$response = array();

	require_once __DIR__ . '/db_config.php';

	$db = new DB_CONNECT();

	$result = mysql_query("SELECT *FROM products" ) or die(mysql_error());

	if(mysql_num_rows($result) > 0){
		$response["products"] = array();

		while($row = mysql_fetch_array($result)){
			$product = array();
			
			$product["pid"] = $result["pid"];
			$product["name"] = $result["name"];
			$product["price"] = $result["price"];
			$product["description"] = $result["description"];
			$product["created_at"] = $result["created_at"];
			$product["updated_at"] = $result["updated_at"];

			array_push($response["product"], $product);
		}

		$response["success"] = 1;
		echo json_encode($response);

	} else {
		$response["success"] = 0;
		$response["message"] = "No products found";
		echo json_encode($response);
	}
 ?>