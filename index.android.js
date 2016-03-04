/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */
'use strict';
import React, {
  AppRegistry,
  Component,
  StyleSheet,
  TouchableHighlight,
  Image,
  Text,
  View
} from 'react-native';

class Botmote extends Component {
  render() {
    return (
      <View style={styles.container}>
      <TouchableHighlight onPress={this._onPressButton}>
        <Image
          style={styles.icon}
          source={require('./assets/Up-104.png')}
        />
      </TouchableHighlight>
      <View style={styles.leftRightContainer}>
        <TouchableHighlight onPress={this._onPressButton}>
          <Image
            style={styles.icon}
            source={require('./assets/Left-104.png')}
          />
        </TouchableHighlight>
        <TouchableHighlight onPress={this._onPressButton}>
          <Image
            style={styles.icon}
            source={require('./assets/Right-104.png')}
          />
        </TouchableHighlight>
      </View>
      <TouchableHighlight onPress={this._onPressButton}>
        <Image
          style={styles.icon}
          source={require('./assets/Down-104.png')}
        />
      </TouchableHighlight>
      </View>
    );
  }

  _onPressButton() {
    console.log('Hi there');
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  leftRightContainer: {
    flexDirection: 'row'
  }
});

AppRegistry.registerComponent('Botmote', () => Botmote);
