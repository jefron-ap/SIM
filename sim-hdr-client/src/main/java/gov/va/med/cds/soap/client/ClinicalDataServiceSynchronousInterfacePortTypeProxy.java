package gov.va.med.cds.soap.client;

public class ClinicalDataServiceSynchronousInterfacePortTypeProxy implements gov.va.med.cds.soap.client.ClinicalDataServiceSynchronousInterfacePortType {
  private String _endpoint = null;
  private gov.va.med.cds.soap.client.ClinicalDataServiceSynchronousInterfacePortType clinicalDataServiceSynchronousInterfacePortType = null;
  
  public ClinicalDataServiceSynchronousInterfacePortTypeProxy() {
    _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
  }
  
  public ClinicalDataServiceSynchronousInterfacePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
  }
  
  private void _initClinicalDataServiceSynchronousInterfacePortTypeProxy() {
    try {
      clinicalDataServiceSynchronousInterfacePortType = (new gov.va.med.cds.soap.client.ClinicalDataServiceSynchronousInterfaceLocator()).getClinicalDataServiceSynchronousInterfaceHttpPort();
      if (clinicalDataServiceSynchronousInterfacePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)clinicalDataServiceSynchronousInterfacePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)clinicalDataServiceSynchronousInterfacePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (clinicalDataServiceSynchronousInterfacePortType != null)
      ((javax.xml.rpc.Stub)clinicalDataServiceSynchronousInterfacePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public gov.va.med.cds.soap.client.ClinicalDataServiceSynchronousInterfacePortType getClinicalDataServiceSynchronousInterfacePortType() {
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType;
  }
  
  public java.lang.String deleteClinicalData(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType.deleteClinicalData(in0, in1, in2);
  }
  
  public java.lang.String createClinicalData(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType.createClinicalData(in0, in1, in2);
  }
  
  public java.lang.String readClinicalData1(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException{
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType.readClinicalData1(in0, in1, in2, in3);
  }
  
  public java.lang.String appendClinicalData(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType.appendClinicalData(in0, in1, in2);
  }
  
  public java.lang.String readClinicalData(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType.readClinicalData(in0, in1, in2);
  }
  
  public boolean isAlive() throws java.rmi.RemoteException{
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType.isAlive();
  }
  
  public java.lang.String updateClinicalData(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException{
    if (clinicalDataServiceSynchronousInterfacePortType == null)
      _initClinicalDataServiceSynchronousInterfacePortTypeProxy();
    return clinicalDataServiceSynchronousInterfacePortType.updateClinicalData(in0, in1, in2);
  }
  
  
}