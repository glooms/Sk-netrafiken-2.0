//
//  ViewController.h
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-10.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>
@import CoreBluetooth;
@import QuartzCore;
#define myUUID @"00000000-0000-0539-0000-000000000539"


@interface ViewController : UIViewController<UITableViewDelegate, UITableViewDataSource,CBCentralManagerDelegate, CBPeripheralDelegate>
@property (nonatomic, strong) CBCentralManager *centralManager;
@property (nonatomic, strong) CBPeripheral     *pPeripheral;
@property (nonatomic, strong) NSString   *data;
@property (weak, nonatomic) IBOutlet UIImageView *background;

@property (weak, nonatomic) IBOutlet UIView *aboutBox;
@property (weak, nonatomic) IBOutlet UIView *settingsBox;

@property (weak, nonatomic) IBOutlet UITableView *tableView;
- (IBAction)pretendBluetooth:(id)sender;
@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (weak, nonatomic) IBOutlet UIView *line1;
@property (weak, nonatomic) IBOutlet UIView *line2;

@property (nonatomic, strong) NSString *connected;

@end

