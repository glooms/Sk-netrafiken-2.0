//
//  ViewController.m
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-10.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import "ViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "CustomCell.h"
#import "Vehicle.h"
#import "Route.h"
#import "RouteViewController.h"

@interface ViewController ()

@property (weak, nonatomic) IBOutlet UILabel *stationLabel;
@property (weak, nonatomic) IBOutlet UIView *animatedBox;
@property NSMutableArray *buses;
@property int count;
@end

@implementation ViewController

-(void)createTempData{
    Route *route1 = [[Route alloc]initWithStation:@"Höjdpunkten" andRoute:[NSMutableArray arrayWithObjects:@"Södra Värn",@"Kungsgatan",@"IKEA", @"Höjdpunkten", @"Delphinen",nil]];
    Route *route2 = [[Route alloc]initWithStation:@"Höjdpunkten" andRoute:[NSMutableArray arrayWithObjects:@"Södra Värn",@"All helgona kyrkan",@"IKEA", @"Höjdpunkten", @"Gambo",nil]];
    Route *route3 = [[Route alloc]initWithStation:@"Höjdpunkten" andRoute:[NSMutableArray arrayWithObjects:@"Södra Värn",@"Kungsgatan",@"Nova", @"Höjdpunkten", @"Boutulfsplatsen",nil]];
    Route *route4 = [[Route alloc]initWithStation:@"Höjdpunkten" andRoute:[NSMutableArray arrayWithObjects:@"Södra Värn",@"Kungsgatan",@"IKEA", @"Höjdpunkten", @"LTH",nil]];
    Route *route5 = [[Route alloc]initWithStation:@"Höjdpunkten" andRoute:[NSMutableArray arrayWithObjects:@"Södra Värn",@"Kungsgatan",@"IKEA", @"Höjdpunkten", @"Lunds centralstation",nil]];
    
    Vehicle *bus1 = [[Vehicle alloc]initVehicleWith:@"Buss 20" andTime:@"12.05" andStations: route1 andType:YES andStatus:0 andExtra:@""];
    Vehicle *bus2 = [[Vehicle alloc]initVehicleWith:@"Buss 169" andTime:@"12.23" andStations: route2 andType:YES andStatus:0 andExtra:@""];
    Vehicle *train = [[Vehicle alloc]initVehicleWith:@"Pågatåg" andTime:@"13.11" andStations: route3 andType:NO andStatus:2 andExtra:@"Inställd"];
    Vehicle *bus3 = [[Vehicle alloc]initVehicleWith:@"Buss 29" andTime:@"13.32" andStations: route4 andType:YES andStatus:0 andExtra:@""];
    Vehicle *bus4 = [[Vehicle alloc]initVehicleWith:@"Buss 20" andTime:@"13.44" andStations: route5 andType:YES andStatus:1 andExtra:@"+5 min"];
    self.buses = [NSMutableArray arrayWithObjects:bus1,bus2,train,bus3,bus4, nil];
    
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self createTempData];
    
    self.animatedBox.layer.cornerRadius = self.animatedBox.frame.size.width/2;
    self.aboutBox.layer.cornerRadius = 6;
    self.settingsBox.layer.cornerRadius = 6;
    self.tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    self.count = 0;
    [self.tableView setHidden:YES];
    [self.map setHidden:YES];
    [self.line1 setHidden:YES];
    [self.line2 setHidden:YES];
    self.animatedBox.alpha = 0.8f;

    //Animation
    CABasicAnimation *scaleAnimation = [CABasicAnimation animationWithKeyPath:@"transform.scale"];
    scaleAnimation.duration = 1.5;
    scaleAnimation.repeatCount = HUGE_VAL;
    scaleAnimation.autoreverses = YES;
    scaleAnimation.fromValue = [NSNumber numberWithFloat:1.1];
    scaleAnimation.toValue = [NSNumber numberWithFloat:0.95];
    
    [self.animatedBox.layer addAnimation:scaleAnimation forKey:@"scale"];
    
    // Scan for all available CoreBluetooth LE devices
    NSArray *services = @[[CBUUID UUIDWithString:myUUID]];
    CBCentralManager *centralManager = [[CBCentralManager alloc] initWithDelegate:self queue:nil];
    [centralManager scanForPeripheralsWithServices:services options:nil];
    self.centralManager = centralManager;
}

-(void)viewDidAppear:(BOOL)animated{
    //TableView
    [self.tableView deselectRowAtIndexPath:[self.tableView.indexPathsForSelectedRows objectAtIndex:0] animated:YES];
    


}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - CBCentralManagerDelegate

// method called whenever you have successfully connected to the BLE peripheral
- (void)centralManager:(CBCentralManager *)central didConnectPeripheral:(CBPeripheral *)peripheral
{
    [peripheral setDelegate:self];
    [peripheral discoverServices:nil];
    self.connected = [NSString stringWithFormat:@"Connected: %@", peripheral.state == CBPeripheralStateConnected ? @"YES" : @"NO"];
    NSLog(@"%@", self.connected);
}

// CBCentralManagerDelegate - This is called with the CBPeripheral class as its main input parameter. This contains most of the information there is to know about a BLE peripheral.
- (void)centralManager:(CBCentralManager *)central didDiscoverPeripheral:(CBPeripheral *)peripheral advertisementData:(NSDictionary *)advertisementData RSSI:(NSNumber *)RSSI
{
    NSString *localName = [advertisementData objectForKey:CBAdvertisementDataLocalNameKey];
    if ([localName length] > 0) {
        NSLog(@"Found the heart rate monitor: %@", localName);
        [self.centralManager stopScan];
        self.pPeripheral = peripheral;
        peripheral.delegate = self;
        [self.centralManager connectPeripheral:peripheral options:nil];
    }
}

// method called whenever the device state changes.
- (void)centralManagerDidUpdateState:(CBCentralManager *)central
{
    // Determine the state of the peripheral
    if ([central state] == CBCentralManagerStatePoweredOff) {
        NSLog(@"CoreBluetooth BLE hardware is powered off");
    }
    else if ([central state] == CBCentralManagerStatePoweredOn) {
        NSLog(@"CoreBluetooth BLE hardware is powered on and ready");
    }
    else if ([central state] == CBCentralManagerStateUnauthorized) {
        NSLog(@"CoreBluetooth BLE state is unauthorized");
    }
    else if ([central state] == CBCentralManagerStateUnknown) {
        NSLog(@"CoreBluetooth BLE state is unknown");
    }
    else if ([central state] == CBCentralManagerStateUnsupported) {
        NSLog(@"CoreBluetooth BLE hardware is unsupported on this platform");
    }
}

#pragma mark - CBPeripheralDelegate

// CBPeripheralDelegate - Invoked when you discover the peripheral's available services.
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverServices:(NSError *)error
{
    for (CBService *service in peripheral.services) {
        NSLog(@"Discovered service: %@", service.UUID);
        [peripheral discoverCharacteristics:nil forService:service];
    }
}

// Invoked when you discover the characteristics of a specified service.
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverCharacteristicsForService:(CBService *)service error:(NSError *)error
{
}

// Invoked when you retrieve a specified characteristic's value, or when the peripheral device notifies your app that the characteristic's value has changed.
- (void)peripheral:(CBPeripheral *)peripheral didUpdateValueForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
}

#pragma mark - Tableview delgate

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.buses.count;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    RouteViewController *view = [self.storyboard instantiateViewControllerWithIdentifier:@"routeview"];
    Vehicle *vehicle = [self.buses objectAtIndex:indexPath.row];
    view.stations = vehicle.stations;
    [self presentViewController:view animated:YES completion:nil];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    //Yellow color
    UIColor * yellow = [UIColor colorWithRed:255/255.0f green:204/255.0f blue:0/255.0f alpha:1.0f];
   
    //Green
    UIColor * green3 = [UIColor colorWithRed:76/255.0f green:217/255.0f blue:100/255.0f alpha:1.0f];
    //Red
    UIColor * red3 = [UIColor colorWithRed:255/255.0f green:49/255.0f blue:74/255.0f alpha:1.0f];
    
    CustomCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    Vehicle *vehicle = [self.buses objectAtIndex:indexPath.row];
    cell.name.text = vehicle.name;
    cell.time.text = vehicle.time;
    if (vehicle.type) {
        cell.typeImage.image = [UIImage imageNamed:@"vehicle.png"];
    }else{
        cell.typeImage.image = [UIImage imageNamed:@"train.png"];
    }
    if (vehicle.status==0) {
        cell.status.backgroundColor = green3;
    }else if(vehicle.status==1){
        cell.status.backgroundColor = yellow;
    }else{
        cell.status.backgroundColor = red3;
    }
    cell.extraInfo.text = vehicle.extra;
    cell.destination.text = [vehicle.stations getDestination];
    return cell;
}

- (IBAction)pretendBluetooth:(id)sender {
    self.count++;
    if (self.count%2==0) {
        self.stationLabel.text = @"Finding bus station...";
        [self.tableView setHidden:YES];
        [self.map setHidden:YES];
        [self.line1 setHidden:YES];
        [self.line2 setHidden:YES];
        [self performOutAnimationForBox];
        [self performOutAnimationForLabel];
        [self.aboutBox setHidden:NO];
        [self.settingsBox setHidden:NO];
        self.animatedBox.alpha = 0.8f;
        self.background.hidden = NO;


    }else{
        [self.animatedBox.layer removeAllAnimations];
        self.animatedBox.alpha = 1.0f;

        CATransition *animation = [CATransition animation];
        animation.type = kCATransitionFade;
        animation.duration = 0.8;
        [self.background.layer addAnimation:animation forKey:nil];
        self.background.hidden = YES;
        [self.aboutBox setHidden:YES];
        [self.settingsBox setHidden:YES];
        self.stationLabel.text = @"Höjdpunkten";
        [self.tableView setHidden:NO];
        [self preformInLabelAnimation];
        [self preformInAnimationForAnimatedBox];

    }
}
-(void)showMap{
    if (self.count>=1) {
        //55.715819, 13.228552
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(55.696917, 13.189921);
        MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(coord, 500, 500);
        [self.map setRegion:region animated:YES];
        [self.map setHidden:NO];
        MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
        point.coordinate = coord;
        point.title = @"Höjdpunkten";
        point.subtitle = @"Busstation";
        
        [self.map addAnnotation:point];
        [self.line1 setHidden:NO];
        [self.line2 setHidden:NO];
    }else{
        [self.map setHidden:NO];
        [self.line1 setHidden:NO];
        [self.line2 setHidden:NO];
    }
    

}

-(void)preformInAnimationForAnimatedBox{
    float duration = 0.4f;
    //Corner radius goes from circle to square
    CABasicAnimation *cornerAnimation = [CABasicAnimation animationWithKeyPath:@"cornerRadius"];
    cornerAnimation.fromValue = [NSNumber numberWithFloat:self.animatedBox.frame.size.width/2];
    cornerAnimation.toValue = [NSNumber numberWithFloat:0];
    cornerAnimation.duration = duration;
    
    //Change translation
    CABasicAnimation* translationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.translation.y"];
    translationAnimation.fromValue = [NSNumber numberWithFloat:self.animatedBox.frame.origin.y-self.animatedBox.frame.size.height/2];
    translationAnimation.toValue = [NSNumber numberWithFloat:0-self.animatedBox.frame.size.height/2-31];
    translationAnimation.duration = duration;
    
    //Changes width from halfscreen to fill the whole width
    CABasicAnimation * widthAnimation = [CABasicAnimation animationWithKeyPath:@"bounds.size.width"];
    widthAnimation.fromValue = [NSNumber numberWithInt:266];
    widthAnimation.toValue = [NSNumber numberWithInt:376];
    widthAnimation.duration = duration;
    
    //Changes height
    CABasicAnimation * heigtAnimation = [CABasicAnimation animationWithKeyPath:@"bounds.size.height"];
    heigtAnimation.fromValue = [NSNumber numberWithInt:266];
    heigtAnimation.toValue = [NSNumber numberWithInt:100];
    heigtAnimation.duration = duration;
    
    //Perform group animation
    CAAnimationGroup *group = [CAAnimationGroup animation];
    group.animations = [NSArray arrayWithObjects:widthAnimation,cornerAnimation,translationAnimation,
                        heigtAnimation, nil];
    group.delegate = self;
    group.duration = duration;
    group.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    group.removedOnCompletion = NO;
    group.fillMode = kCAFillModeForwards;
    [self.animatedBox.layer addAnimation:group forKey:@"InAnimationForAnimatedBox"];
}

-(void)preformInLabelAnimation{
    float duration = 0.4f;
    //Change translation
    CABasicAnimation* translationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.translation.y"];
    translationAnimation.fromValue = [NSNumber numberWithFloat:self.stationLabel.frame.origin.y-self.stationLabel.frame.size.height/2];
    translationAnimation.toValue = [NSNumber numberWithFloat:-80];
    translationAnimation.duration = duration;
    //Change translation
    CABasicAnimation * widthAnimation = [CABasicAnimation animationWithKeyPath:@"bounds.size.width"];
    widthAnimation.fromValue = [NSNumber numberWithInt:259];
    widthAnimation.toValue = [NSNumber numberWithInt:150];
    widthAnimation.duration = duration;
    
    //Perform group animation
    CAAnimationGroup *group = [CAAnimationGroup animation];
    group.animations = [NSArray arrayWithObjects:translationAnimation,widthAnimation, nil];
    group.delegate = self;
    group.duration = duration;
    group.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    group.removedOnCompletion = NO;
    group.fillMode = kCAFillModeForwards;
    [self.stationLabel.layer addAnimation:group forKey:@"InAnimationForLabel"];
    self.stationLabel.numberOfLines = 1;
    
    [self performSelector:@selector(showMap) withObject:nil afterDelay:0.4 inModes:[NSArray arrayWithObject:NSRunLoopCommonModes]];
    
    CGRect t = self.tableView.frame;
    self.tableView.frame = CGRectMake(0, 1000, t.size.width, t.size.width);
    [UIView animateWithDuration:1.5
                     animations:^{
                         self.tableView.frame = t;
                     }];
}

-(void)performOutAnimationForBox{
    float duration = 0.4f;
    //Corner radius goes from circle to square
    CABasicAnimation *cornerAnimation = [CABasicAnimation animationWithKeyPath:@"cornerRadius"];
    cornerAnimation.fromValue =[NSNumber numberWithFloat:0];
    cornerAnimation.toValue = [NSNumber numberWithFloat:self.animatedBox.frame.size.width/2];
    cornerAnimation.duration = duration;
    
    //Change translation
    CABasicAnimation* translationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.translation.y"];
    translationAnimation.fromValue =[NSNumber numberWithFloat:0-self.animatedBox.frame.size.height/2-31];
    translationAnimation.toValue = [NSNumber numberWithFloat: 26];
    translationAnimation.duration = duration;
    
    //Changes width from halfscreen to fill the whole width
    CABasicAnimation * widthAnimation = [CABasicAnimation animationWithKeyPath:@"bounds.size.width"];
    widthAnimation.fromValue =[NSNumber numberWithInt:376];
    widthAnimation.toValue = [NSNumber numberWithInt:266];
    widthAnimation.duration = duration;
    
    //Changes height
    CABasicAnimation * heigtAnimation = [CABasicAnimation animationWithKeyPath:@"bounds.size.height"];
    heigtAnimation.fromValue =[NSNumber numberWithInt:100];
    heigtAnimation.toValue = [NSNumber numberWithInt:266];
    heigtAnimation.duration = duration;
    
    //Perform group animation
    CAAnimationGroup *group = [CAAnimationGroup animation];
    group.animations = [NSArray arrayWithObjects:widthAnimation,cornerAnimation,translationAnimation,
                        heigtAnimation, nil];
    group.delegate = self;
    group.duration = duration;
    group.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    group.removedOnCompletion = NO;
    group.fillMode = kCAFillModeForwards;
    [self.animatedBox.layer addAnimation:group forKey:@"InAnimationForAnimatedBox"];
    
    //Animation
    CABasicAnimation *scaleAnimation = [CABasicAnimation animationWithKeyPath:@"transform.scale"];
    scaleAnimation.duration = 1.5;
    scaleAnimation.repeatCount = HUGE_VAL;
    scaleAnimation.autoreverses = YES;
    scaleAnimation.fromValue = [NSNumber numberWithFloat:1.1];
    scaleAnimation.toValue = [NSNumber numberWithFloat:0.95];
    [self.animatedBox.layer addAnimation:scaleAnimation forKey:@"scale"];
}

-(void)performOutAnimationForLabel{
    float duration = 0.4f;
    //Change translation
    CABasicAnimation* translationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.translation.y"];
    translationAnimation.toValue = [NSNumber numberWithFloat:+10];
    translationAnimation.duration = duration;
    
    //Perform group animation
    CAAnimationGroup *group = [CAAnimationGroup animation];
    group.animations = [NSArray arrayWithObjects:translationAnimation, nil];
    group.delegate = self;
    group.duration = duration;
    group.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear];
    group.removedOnCompletion = NO;
    group.fillMode = kCAFillModeForwards;
    [self.stationLabel.layer addAnimation:group forKey:@"InAnimationForLabel"];
    self.stationLabel.numberOfLines = 2;
}
@end
