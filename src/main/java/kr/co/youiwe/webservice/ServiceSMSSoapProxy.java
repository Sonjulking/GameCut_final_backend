package kr.co.youiwe.webservice;

public class ServiceSMSSoapProxy implements ServiceSMSSoap {
  private String _endpoint = null;
  private ServiceSMSSoap serviceSMSSoap = null;
  
  public ServiceSMSSoapProxy() {
    _initServiceSMSSoapProxy();
  }
  
  public ServiceSMSSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initServiceSMSSoapProxy();
  }
  
  private void _initServiceSMSSoapProxy() {
    try {
      serviceSMSSoap = (new ServiceSMSLocator()).getServiceSMSSoap();
      if (serviceSMSSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)serviceSMSSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)serviceSMSSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (serviceSMSSoap != null)
      ((javax.xml.rpc.Stub)serviceSMSSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ServiceSMSSoap getServiceSMSSoap() {
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap;
  }
  
  public String sendSMS(String smsID, String hashValue, String senderPhone, String receivePhone, String smsContent) throws java.rmi.RemoteException{
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap.sendSMS(smsID, hashValue, senderPhone, receivePhone, smsContent);
  }
  
  public String sendSMSwnc(String smsID, String hashValue, String gubun, String senderPhone, String receivePhone, String smsContent) throws java.rmi.RemoteException{
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap.sendSMSwnc(smsID, hashValue, gubun, senderPhone, receivePhone, smsContent);
  }
  
  public String sendSMSReserve(String smsID, String hashValue, String senderPhone, String receivePhone, String smsContent, String reserveDate, String reserveTime, String userDefine) throws java.rmi.RemoteException{
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap.sendSMSReserve(smsID, hashValue, senderPhone, receivePhone, smsContent, reserveDate, reserveTime, userDefine);
  }
  
  public String sendSMSCallBack(String smsID, String hashValue, String senderPhone, String receivePhone, String callbackUrl, String smsContent) throws java.rmi.RemoteException{
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap.sendSMSCallBack(smsID, hashValue, senderPhone, receivePhone, callbackUrl, smsContent);
  }
  
  public String sendSMSCallBackReserve(String smsID, String hashValue, String senderPhone, String receivePhone, String callbackUrl, String smsContent, String reserveDate, String reserveTime, String userDefine) throws java.rmi.RemoteException{
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap.sendSMSCallBackReserve(smsID, hashValue, senderPhone, receivePhone, callbackUrl, smsContent, reserveDate, reserveTime, userDefine);
  }
  
  public int reserveCancle(String smsID, String hashValue, String searchValue, String mode) throws java.rmi.RemoteException{
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap.reserveCancle(smsID, hashValue, searchValue, mode);
  }
  
  public int getRemainCount(String smsID, String hashValue) throws java.rmi.RemoteException{
    if (serviceSMSSoap == null)
      _initServiceSMSSoapProxy();
    return serviceSMSSoap.getRemainCount(smsID, hashValue);
  }
  
  
}