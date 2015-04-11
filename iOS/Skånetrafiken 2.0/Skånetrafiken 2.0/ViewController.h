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


@interface ViewController : UIViewController<CLLocationManagerDelegate, UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) CLBeaconRegion *myBeaconRegion;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
- (IBAction)pretendBluetooth:(id)sender;
@property (weak, nonatomic) IBOutlet MKMapView *map;
@property (weak, nonatomic) IBOutlet UIView *line1;
@property (weak, nonatomic) IBOutlet UIView *line2;

@end

