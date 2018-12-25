pragma solidity ^0.5.1;

contract UpdateManager {
    address networkOwner;
    
    mapping (string => address) private _fullNodes;
    mapping (string => address) private _endPeerOwners;
    mapping (string => _latestUpdate) private _latestUpdates;
    mapping (string => _update) private _ongoingUpdates;
    
    struct _latestUpdate {
        string _updateFileName;
        string _timeStamp;
        bool _initialized;
    }
    
    struct _update {
        address _updateOwner;
        string _updateFileName;
        string _updateFileHash;
        string _winnerBidderIP;
        uint64 _price;
        uint64 _transferPrice;
        uint40 _bandWidth;
        bool _ongoing;
    }
    
    constructor() public{
        networkOwner = msg.sender;
    }
    
    function registerFullNode(string memory fullNodeIP) public fullNodeNotRegisteredBefore(fullNodeIP){
        _fullNodes[fullNodeIP] = msg.sender;
    }
    
    function registerEndPeer(string memory endPeerIP) public endPeerNotRegisteredBefore(endPeerIP){
        _endPeerOwners[endPeerIP] = msg.sender;
    }
    
    function getOngoingUpdateInfo(string memory endPeerIP) public view returns(string memory, uint64, uint40){
        if(_ongoingUpdates[endPeerIP]._ongoing){
            return (_ongoingUpdates[endPeerIP]._winnerBidderIP, _ongoingUpdates[endPeerIP]._transferPrice, _ongoingUpdates[endPeerIP]._bandWidth);
        }else{
            return ("-", 0, 0);
        }
    }
    
    function getLatestUpdateInfo(string memory endPeerIP) public view returns(string memory, string memory, string memory){
        if(_latestUpdates[endPeerIP]._initialized){
            return (endPeerIP, _latestUpdates[endPeerIP]._updateFileName, _latestUpdates[endPeerIP]._timeStamp);
        }else{
            return (endPeerIP, "-", "-");
        }
    }
    
    function requestUpdate(string memory deviceIP, string memory updateFileName, string memory updateFileHash, uint64 price) public ongoingUpdateStatus(deviceIP, false) endPeerOwnerOnly(deviceIP){
        _ongoingUpdates[deviceIP]._updateFileName = updateFileName;
        _ongoingUpdates[deviceIP]._updateFileHash = updateFileHash;
        _ongoingUpdates[deviceIP]._updateOwner = msg.sender;
        _ongoingUpdates[deviceIP]._price = price;
        _ongoingUpdates[deviceIP]._ongoing = true;
        // _ongoingUpdates[endPeerIP]._requestTimeStamp = now;
    }
    
    function bidForUpdate(string memory fullNodeIP, string memory target, uint64 price, uint40 bandWidth) public validUpdateBidder(fullNodeIP) ongoingUpdateStatus(target, true) fullNodeOwnerOnly(fullNodeIP) {
        if(_ongoingUpdates[target]._transferPrice == 0){
            _ongoingUpdates[target]._winnerBidderIP = fullNodeIP;
            _ongoingUpdates[target]._transferPrice = price;
            _ongoingUpdates[target]._bandWidth = bandWidth;
        }else if(price < _ongoingUpdates[target]._transferPrice){
            _ongoingUpdates[target]._winnerBidderIP = fullNodeIP;
            _ongoingUpdates[target]._transferPrice = price;
            _ongoingUpdates[target]._bandWidth = bandWidth;
        }
    }
    
    function cancelUpdate(string memory deviceIP) public endPeerOwnerOnly(deviceIP){
        delete(_ongoingUpdates[deviceIP]);
    }
    
    function finalizeUpdate(string memory endPeerIP, string memory timeStamp) public networkOwnerOnly{
        _latestUpdates[endPeerIP]._updateFileName = _ongoingUpdates[endPeerIP]._updateFileName;
        _latestUpdates[endPeerIP]._timeStamp = timeStamp;
        _latestUpdates[endPeerIP]._initialized = true;
        delete _ongoingUpdates[endPeerIP];
    }
    
    function getWinnerInfo(string memory endPeerIP) public view networkOwnerOnly hasWinner(endPeerIP) returns(address, uint64, address, uint64){
        return (_fullNodes[_ongoingUpdates[endPeerIP]._winnerBidderIP],  _ongoingUpdates[endPeerIP]._transferPrice, _ongoingUpdates[endPeerIP]._updateOwner, _ongoingUpdates[endPeerIP]._price);
    }
    
    modifier hasWinner(string memory endPeerIP) {
        require(_ongoingUpdates[endPeerIP]._transferPrice != 0);
        _;
    }
    
    modifier endPeerOwnerOnly(string memory deviceIP) {
        require(_endPeerOwners[deviceIP] == msg.sender);
        _;
    }
    
    modifier fullNodeOwnerOnly(string memory fullNodeIP) {
        require(_fullNodes[fullNodeIP] == msg.sender);
        _;
    }
    
    modifier endPeerNotRegisteredBefore(string memory endPeerIP) {
        require(_endPeerOwners[endPeerIP] == address(0));
        _;
    }
    
    modifier fullNodeNotRegisteredBefore(string memory fullNodeIP){
        require(_fullNodes[fullNodeIP] == address(0));
        _;
    }
    
    modifier ongoingUpdateStatus(string memory target, bool status) {
        require(_ongoingUpdates[target]._ongoing == status);
        _;
    }
    
    modifier validUpdateBidder(string memory fullNodeIP) {
        require(_fullNodes[fullNodeIP] != address(0));
        _;
    }
    
    modifier networkOwnerOnly() {
        require(msg.sender == networkOwner && networkOwner!= address(0));
        _;
    }
}