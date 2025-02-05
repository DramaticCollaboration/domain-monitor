<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">

  <title>Domain and SSL Certificate Report</title>

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
                    <h1>Domain and SSL Certificate Expiry Report</h1>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td align="center" valign="top">
              <table border="0" cellpadding="10" cellspacing="0" width="100%" id="emailBody">
                <tr>
                  <td align="center" valign="top">
                    <h2>Domain Expiry</h2>
                  </td>
                </tr>
                <tr>
                  <td align="center" valign="top">
                    <table align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse:collapse;border:none">
                      <tbody>
                        <tr style="border:solid #5b9bd5 1.0pt;background:#5b9bd5;padding:0in 5.4pt 0in 5.4pt">
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Domain Name</span></b></p>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Registrar</span></b></p>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Created</span></b>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Expiry</span></b>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Days Left</span></b>
                          </td>
                        </tr>
                      <#assign row=0>
	                  <#list domains as domain>
                      <#if (row % 2) == 0>
                        <tr style="background:#deeaf6;">
                      <#else>
                        <tr>
                      </#if>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${domain.name}
                          </td>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${domain.registrar}
                          </td>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${domain.createDate?string["yyyy/MM/dd"]}
                          </td>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${domain.expiryDate?string["yyyy/MM/dd"]}
                          </td>
                          <td align="center" valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${domain.expiryDays}
                          </td>
                        </tr>
                      </tbody>
                      <#assign row = row + 1>
                      </#list>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td align="center" valign="top">
                    <h2>SSL Certificate Expiry</h2>
                  </td>
                </tr>
                <tr>
                  <td align="center" valign="top">
                    <table align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse:collapse;border:none">
                      <tbody>
                        <tr style="border:solid #5b9bd5 1.0pt;background:#5b9bd5;padding:0in 5.4pt 0in 5.4pt">
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Certificate Name</span></b></p>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Subject</span></b>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Issuer</span></b>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Issued</span></b>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Expiry</span></b>
                          </td>
                          <td>
                            <p class="MsoNormal"><b><span style="color:white;">Days Left</span></b>
                          </td>
                        </tr>
                      <#assign row=0>
                      <#list certificates as certificate> 
                      <#if (row % 2) == 0>
                        <tr style="background:#deeaf6;">
                      <#else>
                        <tr>
                      </#if>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${certificate.name}
                          </td>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${certificate.subject}
                          </td>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${certificate.issuer}
                          </td>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${certificate.createDate?string["yyyy/MM/dd"]}
                          </td>
                          <td valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${certificate.expiryDate?string["yyyy/MM/dd"]}
                          </td>
                          <td align="center" valign="top" style="border:solid #9cc2e5 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt">
                            ${certificate.expiryDays}
                          </td>
                        </tr>
                      </tbody>
                      <#assign row = row + 1>
                      </#list>
                    </table>
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