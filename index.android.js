// @flow
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */
'use strict';
import React, {
  AppRegistry,
  Component,
  StyleSheet,
  ListView,
  TouchableHighlight,
  Image,
  Text,
  Navigator,
  View,
  BackAndroid,
  DeviceEventEmitter
} from 'react-native';

import BluetoothLE from './lib/bluetoothle';

class DeviceList extends Component {
  constructor() {
    super();
    const self = this;
    this.renderRow = this.renderRow.bind(this);
    this.pressRow = this.pressRow.bind(this);
    this.state = {
      dataSource: new ListView.DataSource({
        rowHasChanged: (row1, row2) => row1 !== row2,
      })
    };
  }

  componentDidMount() {
    console.log(`props ${this.props}`);
    this.setState({
      dataSource: this.state.dataSource.cloneWithRows(this.props.devices)
    });
  }

  renderRow(rowData, sectionID, rowID) {
    return (
      <TouchableHighlight onPress={() => this.pressRow(rowID)}>
        <View>
          <View style={styles.listRow}>
            <Text>{rowData}</Text>
          </View>
        </View>
      </TouchableHighlight>
    );
  }

  render() {
    return (
      <View style={styles.devicesList}>
        <ListView
          dataSource ={this.state.dataSource}
          renderRow={this.renderRow}
          />
      </View>
    );
  }

  pressRow(rowID) {
    console.log(`selected device: ${selectedDevice}`);
    const selectedDevice = this.props.devices[rowID];
    this.props.onSelectDevice(selectedDevice);
  }
}

class Botmote extends Component {
  state = {
    devices: []
  };
  constructor() {
    super();
    this.didPressRightButton = this.didPressRightButton.bind(this);
  }

  render() {
    console.log('rendering!');
    return (
      <View style={styles.container}>
        {/* Up Button*/}
        <View underlayColor='#F5FCFF'
              onStartShouldSetResponder={() => true}
              onResponderMove={this.didPressUpButton}
              onResponderRelease={this.didEndButtonPress}>
          <Image
            style={styles.icon}
            source={require('./assets/Up-104.png')}
          />
        </View>
        {/* Container for left and right buttons */}
        <View style={styles.leftRightContainer}>
          {/* left button */}
          <View underlayColor='#F5FCFF'
                onStartShouldSetResponder={() => true}
                onResponderMove={this.didPressLeftButton}
                onResponderRelease={this.didEndButtonPress}>
            <Image
              style={styles.icon}
              source={require('./assets/Left-104.png')}
            />
          </View>
          {/* right button */}
          <View underlayColor='#F5FCFF'
                onStartShouldSetResponder={() => true}
                onResponderMove={this.didPressRightButton}
                onResponderRelease={this.didEndButtonPress}>
            <Image
              style={styles.icon}
              source={require('./assets/Right-104.png')}
            />
          </View>
        </View>
        {/* down button */}
        <View underlayColor='#F5FCFF'
              onStartShouldSetResponder={() => true}
              onResponderMove={this.didPressDownButton}
              onResponderRelease={this.didEndButtonPress}>
          <Image
            style={styles.icon}
            source={require('./assets/Down-104.png')}
          />
        </View>
        <View style={styles.showDevicesContainer}>
          <TouchableHighlight underlayColor='#F5FCFF' onPress={() => {
            this.props.navigator.push({ id: 'deviceList' });
          }}>
            <Text>Show Available Devices</Text>
          </TouchableHighlight>
          <Text>Selected Device: {this.props.selectedDevice ? this.props.selectedDevice : "No Device Connected"}</Text>
        </View>
      </View>
    );
  }

  //Button actions
  didPressUpButton() {
    console.log('Move forward');
    BluetoothLE.moveUp();
  }

  didPressDownButton() {
    console.log('Move backward');
    BluetoothLE.moveDown();
  }

  didPressLeftButton() {
    console.log('Move left');
    BluetoothLE.moveLeft();
  }

  didPressRightButton() {
    console.log('Move right');
    BluetoothLE.moveRight();
  }

  didEndButtonPress() {
    console.log('Stop moving');
    BluetoothLE.stopMoving();
  }
}

class BotmoteNavigator extends Component {
  onMainScreen = true;

  state = {
    devices: []
  };

  constructor() {
    super();
    this.renderScene = this.renderScene.bind(this);
    this._setNavigatorRef = this._setNavigatorRef.bind(this);
    this.onDeviceSelected = this.onDeviceSelected.bind(this);
  }

  componentDidMount() {
    BackAndroid.addEventListener('hardwareBackPress', () => {
      if (!this.onMainScreen) {
        this._navigator.pop();
        return true;
      }
      return false;
    });

    DeviceEventEmitter.addListener('foundDevices', (e) => {
      const devices = e.deviceList;
      const devicesArray = [];
      for (let i = 0; i < devices.length; i++) {
        devicesArray.push(devices[i]);
      }
      this.setState({devices: devices});
      console.log(devicesArray);
    });

    DeviceEventEmitter.addListener('deviceConnection', (connection) => {
      const status = connection.status;
      console.log('connected to device!!!!!!');
    });
    BluetoothLE.start();
  }

  renderScene(route, nav) {
    switch (route.id) {
      case 'deviceList':
        this.onMainScreen = false;
        return <DeviceList onSelectDevice={this.onDeviceSelected} navigator={nav} devices={this.state.devices} />;
      default:
        this.onMainScreen = true;
        return (
          <Botmote selectedDevice={this.state.selectedDevice} navigator={nav}/>
        );
    }
  }

  render() {
    return (
      <View style={styles.navigator}>
        <Navigator
          ref={this._setNavigatorRef}
          initialRoute={{id: 'home'}}
          renderScene={this.renderScene}
          configureScene={(route) => {
            return Navigator.SceneConfigs.FloatFromBottom;
          }} />
      </View>
    );
  }

  onDeviceSelected(device) {
    this.setState({
      selectedDevice: device
    });
    BluetoothLE.connectDevice(0);
    this._navigator.pop();
  }

  _setNavigatorRef(navigator) {
    if (navigator !== this._navigator) {
      this._navigator = navigator;
    }
  }
}


const styles = StyleSheet.create({
  navigator: {
    flex: 1,
    flexDirection: 'column'
  },
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },
  listRow: {
    height: 150,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  devicesList: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: '#FFA500'
  },
  showDevicesContainer: {
    marginTop: 100,
    flexDirection: 'column',
    alignItems: 'center'
  },
  leftRightContainer: {
    flexDirection: 'row'
  },
  listView: {
    flex: 1,
    paddingTop: 20,
    backgroundColor: '#F5FCFF',
  },
});

AppRegistry.registerComponent('Botmote', () => BotmoteNavigator);
