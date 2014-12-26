function ORMMAReady12()
{
VservAdControllerInterface.showMsg('*********ORMMAReady12 Called');
if(typeof(vservImpressionNotifyURL)!='undefined' && typeof(vservImpressionNotifyURL!=null) && vservImpressionNotifyURL instanceof Array)
{
	VservAdControllerInterface.showMsg('inside recordImpression  Array--notifyOnce');
	for(var i in vservImpressionNotifyURL)
	{
		VservAdControllerInterface.populateCacheNotifyUrlsToHashTable(vservImpressionNotifyURL[i]);
	}
	VservAdControllerInterface.SaveUrlsOnce();
}
else if(typeof(vservImpressionNotifyURL)!='undefined' && typeof(vservImpressionNotifyURL!=null))
{
	VservAdControllerInterface.showMsg('inside recordImpression Single--notifyOnce');
	VservAdControllerInterface.populateCacheNotifyUrlsToHashTable(vservImpressionNotifyURL);
	VservAdControllerInterface.SaveUrlsOnce();
}
else{
	VservAdControllerInterface.showMsg('inside recordImpression Single--No notifyOnce');
	VservAdControllerInterface.populateCacheNotifyUrlsToHashTable("");
	VservAdControllerInterface.SaveUrlsOnce();
}

}