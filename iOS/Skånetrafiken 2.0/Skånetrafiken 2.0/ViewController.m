//
//  ViewController.m
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-10.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import "ViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface ViewController ()

@property (weak, nonatomic) IBOutlet UILabel *stationLabel;
@property (weak, nonatomic) IBOutlet UIView *animatedBox;
@property NSMutableArray *buses;
@property int count;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    // Initialize location manager and set ourselves as the delegate
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    
    // Create a NSUUID with the same UUID as the broadcasting beacon
    NSUUID *uuid = [[NSUUID alloc] initWithUUIDString:@"A77A1B68-49A7-4DBF-914C-760D07FBB87B"];
    
    // Setup a new region with that UUID and same identifier as the broadcasting beacon
    self.myBeaconRegion = [[CLBeaconRegion alloc] initWithProximityUUID:uuid
                                                             identifier:@"com.jacobarvidsson.region"];
    
    // Tell location manager to start monitoring for the beacon region
    [self.locationManager startMonitoringForRegion:self.myBeaconRegion];
    
    self.animatedBox.layer.cornerRadius = self.animatedBox.frame.size.width/2;
    self.buses = [NSMutableArray arrayWithObjects:@"Bus 20",@"Bus 169",@"Bus 6",@"Bus 20",@"Bus 169", nil];
    self.tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    self.count = 0;
    [self.tableView setHidden:YES];

}

-(void)viewDidAppear:(BOOL)animated{
    //TableView
    [self.tableView deselectRowAtIndexPath:[self.tableView.indexPathsForSelectedRows objectAtIndex:0] animated:YES];
    
    //Animation
    CABasicAnimation *scaleAnimation = [CABasicAnimation animationWithKeyPath:@"transform.scale"];
    scaleAnimation.duration = 1.5;
    scaleAnimation.repeatCount = HUGE_VAL;
    scaleAnimation.autoreverses = YES;
    scaleAnimation.fromValue = [NSNumber numberWithFloat:1.1];
    scaleAnimation.toValue = [NSNumber numberWithFloat:0.95];
    
    [self.animatedBox.layer addAnimation:scaleAnimation forKey:@"scale"];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark LocationDelgateMethods

- (void)locationManager:(CLLocationManager*)manager didEnterRegion:(CLRegion*)region
{
    [self.locationManager startRangingBeaconsInRegion:self.myBeaconRegion];
}

-(void)locationManager:(CLLocationManager*)manager didExitRegion:(CLRegion*)region
{
    [self.locationManager stopRangingBeaconsInRegion:self.myBeaconRegion];
    //self.beaconFoundLabel.text = @"No";
}

-(void)locationManager:(CLLocationManager*)manager
       didRangeBeacons:(NSArray*)beacons
              inRegion:(CLBeaconRegion*)region
{
    // Beacon found!
    self.stationLabel.text = @"Lunds Centralstation";
    NSLog(@"test");
    //CLBeacon *foundBeacon = [beacons firstObject];
    // You can retrieve the beacon data from its properties
    //NSString *uuid = foundBeacon.proximityUUID.UUIDString;
    //NSString *major = [NSString stringWithFormat:@"%@", foundBeacon.major];
    //NSString *minor = [NSString stringWithFormat:@"%@", foundBeacon.minor];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.buses.count;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    cell.textLabel.text = [self.buses objectAtIndex:indexPath.row] ;
    return cell;
}

- (IBAction)pretendBluetooth:(id)sender {
    self.count++;
    if (self.count%2==0) {
        self.stationLabel.text = @"Finding bus station...";
        [self.tableView setHidden:YES];
    }else{
        [self.animatedBox.layer removeAllAnimations];
        self.stationLabel.text = @"Lunds station";
        [self.tableView setHidden:NO];
        
        CABasicAnimation *cornerAnimation = [CABasicAnimation animationWithKeyPath:@"cornerRadius"];
        //cornerAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
        //cornerAnimation.fromValue = [NSNumber numberWithFloat:self.animatedBox.frame.size.width/2];
        cornerAnimation.toValue = [NSNumber numberWithFloat:0];
        cornerAnimation.duration = 1.0;
        
        CABasicAnimation *anim1 = [CABasicAnimation animationWithKeyPath:@"cornerRadius"];
        anim1.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
        anim1.fromValue = [NSNumber numberWithFloat:self.animatedBox.frame.size.width/2];
        anim1.toValue = [NSNumber numberWithFloat:0];
        anim1.duration = 2.0;
        
        /*
        CABasicAnimation* translationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.translation.y"];
        translationAnimation.toValue = [NSNumber numberWithFloat:-100];
        translationAnimation.duration = 1.5;
        translationAnimation.repeatCount = 1.0;
        translationAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseOut];

        CABasicAnimation *scaleAnimation = [CABasicAnimation animationWithKeyPath:@"transform.scale.x"];
        scaleAnimation.duration = 1.5f;
        scaleAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
        scaleAnimation.fromValue = [NSNumber numberWithFloat:1.0f];
        scaleAnimation.toValue = [NSNumber numberWithFloat:2.0f];

        */
        //[self.animatedBox.layer.mask addAnimation:animation forKey:@"animateMask"];
        
        CAAnimationGroup *group = [CAAnimationGroup animation];
        group.animations = [NSArray arrayWithObjects:anim1, nil]; //translationAnimation,scaleAnimation,
        group.delegate = self;
        //group.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
        group.removedOnCompletion = NO;
        group.fillMode = kCAFillModeForwards;
        [self.animatedBox.layer addAnimation:group forKey:@"randt"];
        self.animatedBox.layer.cornerRadius = 0;
        //self.animatedBox.frame = CGRectMake(self.animatedBox.frame.size.width, self.animatedBox.frame.size.height, self.animatedBox.frame.origin.x, self.animatedBox.frame.origin.y-100);

    }
}
@end
