<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">

  <title>Certificate Expiry Alert</title>

  <style type="text/css">
    td {padding: 5px;}
  </style>
  <!--[if mso]>
  <style type="text/css">
    body, table, td {font-family: Arial, Helvetica, sans-serif !important;}
  </style>
  <![endif]-->  
</head>
<body style="margin:0; padding:0;">
  <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" id="bodyTable">
    <tr>
      <td align="center" valign="top">
        <table border="0" cellpadding="10" cellspacing="0" width="800" id="emailContainer">
          <tr>
            <td align="center" valign="top">
              <table border="0" cellpadding="10" cellspacing="0" width="100%" id="emailHeader">
                <tr>
                  <td align="center" valign="top">  
                    <h1>Certificate Expiry Alert</h1>
                    <p>The following certificate is due to expire in <strong>${certificate.expiryDays}</strong> days: <code><strong>${certificate.name}</code></strong></p>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td align="center" valign="top">
              <table border="0" cellpadding="10" cellspacing="0" width="100%" id="emailFooter">
                <tr>
                  <td align="center" valign="top">
                    &copy; empasy.com
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>