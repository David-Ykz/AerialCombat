// Draw sprites to be larger than their logical size, for smoother collisions.
const RENDER_SCALE = 1.2;

class Draw {
  constructor(onImagesLoadedFn) {
    this.loadGameImages(onImagesLoadedFn);
    this.storeImages = this.storeImages.bind(this);
  }

  storeImages(loadedImages) {
    this.cloudImg = loadedImages['cloud.png'];
    this.parachuteImg = loadedImages['parachute.png'];

    this.airplaneImages = {
      'airplaneWhite': loadedImages['airplane_white.png'],
      'airplaneBlack': loadedImages['airplane_black.png']
    };

    this.powerupImages = {
      'bomb': loadedImages['bomb_powerup.png'],
      'medkit': loadedImages['med_box_powerup.png'],
      'rocket': loadedImages['rocket_powerup.png'],
      'shrapnel': loadedImages['shrapnel_powerup.png'],
      'tripleshot': loadedImages['tripleshot_powerup.png']
    };

    this.projectileImages = {
      'bullet': loadedImages['bullet_projectile.png'],
      'bomb': loadedImages['bomb_projectile.png'],
      'rocket': loadedImages['rocket_projectile.png'],
      'shrapnel': loadedImages['shrapnel_projectile.png'],
      'tripleshot': loadedImages['bullet_projectile.png'],
    };

    this.currentWeaponImages = {
      'bombweapon': loadedImages['bomb_powerup.png'],
      'rocketweapon': loadedImages['rocket_powerup.png'],
      'shrapnelweapon': loadedImages['shrapnel_powerup.png'],
      'tripleshotweapon': loadedImages['tripleshot_powerup.png'],
    };
  }

  imageDict(src, width, height) {
    var img = {url: src};
    if (width != null) {
      img['width'] = width;
    }
    if (height != null) {
      img['height'] = height;
    }
    return img;
  }

  loadGameImages(onImagesLoadedFn) {
    var images = [
      this.imageDict('cloud.png'),
      this.imageDict('parachute.png', 32, 32),
      this.imageDict('airplane_black.png', 32, 32),
      this.imageDict('airplane_white.png', 32, 32),
      this.imageDict('airplane_white.png', 32, 32),
      this.imageDict('airplane_black.png', 32, 32),
      this.imageDict('bomb_powerup.png', 32, 32),
      this.imageDict('bomb_projectile.png', 32, 32),
      this.imageDict('med_box_powerup.png', 32, 32),
      this.imageDict('rocket_powerup.png', 32, 32),
      this.imageDict('rocket_projectile.png', 32, 32),
      this.imageDict('shrapnel_powerup.png', 32, 32),
      this.imageDict('shrapnel_projectile.png', 32, 32),
      this.imageDict('tripleshot_powerup.png', 32, 32),
      this.imageDict('bullet_projectile.png', 32, 32),
    ];
    var loadedImages = {};
    var promiseArray = images.map(function(imgData) {
       var prom = new Promise(function(resolve,reject){
           var img;
           if (imgData.width != null && imgData.height != null) {
             img = new Image(imgData.width, imgData.height);
           } else {
             img = new Image();
           }
           img.src = imgData.url;
           img.onload = function(){
               loadedImages[imgData.url] = img;
               resolve();
           };
       });
       return prom;
    });

    Promise.all(promiseArray).then(() => {
      this.storeImages(loadedImages);
      onImagesLoadedFn();
    });
  }

  drawAirplane(ctx, airplaneColor, xCenter, yCenter, angle, width, height) {
    const image = this.airplaneImages[airplaneColor];
    const imgWidth = width == null ? image.width : width;
    const imgHeight = height == null ? image.height : height;
    ctx.setTransform(1, 0, 0, 1, xCenter, yCenter); // sets scale and origin
    ctx.rotate(-angle * Math.PI / 180);
    ctx.drawImage(image, -imgWidth / 2, -imgHeight / 2, imgWidth * RENDER_SCALE, imgHeight * RENDER_SCALE);
    ctx.setTransform(1, 0, 0, 1, 0, 0);
  }

  drawPowerUp(ctx, powerup, xCenter, yCenter, width, height) {
    this.drawImage(ctx, this.parachuteImg, xCenter, yCenter - height, width, height);
    this.drawImage(ctx, this.powerupImages[powerup.name], xCenter, yCenter, width, height);
  }

  drawProjectile(ctx, projectile, x, y) {
    ctx.setTransform(1, 0, 0, 1, x, y);
    ctx.rotate(-projectile.angle * Math.PI / 180);
    ctx.drawImage(this.projectileImages[projectile.name], -projectile.radius, -projectile.radius, projectile.radius * 2 * RENDER_SCALE, projectile.radius * 2 * RENDER_SCALE);
    ctx.setTransform(1, 0, 0, 1, 0, 0);
  }

  drawCurrentUserWeapon(ctx, myPlayer) {
    var currentWeaponImg = this.currentWeaponImages[myPlayer.weapon];
    if (currentWeaponImg != null) {
      this.drawImage(ctx, currentWeaponImg, 50, 50, 64, 64);
    }
  }

  drawImage(ctx, image, xCenter, yCenter, width, height) {
    const imgWidth = width == null ? image.width : width;
    const imgHeight = height == null ? image.height : height;
    ctx.drawImage(image, xCenter - width / 2, yCenter - height / 2, imgWidth, imgHeight);
  }

  drawCircle(ctx, x, y, r, color='#b8e015') {
    ctx.beginPath();
    ctx.arc(x, y, r, 0, Math.PI*2);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
  }

  drawText(ctx, x, y, text, textAlign='left', color='black', font='30px Arial') {
    ctx.font = font;
    ctx.fillStyle = color;
    ctx.textAlign = textAlign;
    ctx.fillText(text, x, y);
  }

  drawHealthBar(ctx, x, y, health) {
    ctx.beginPath();
    ctx.rect(x - 75/2, y - 30, 75, 10);
    ctx.fillStyle = "#ff1100";
    ctx.fill();
    ctx.closePath();

    ctx.beginPath();
    ctx.rect(x - 75/2, y - 30, (health/4)*3, 10);
    ctx.fillStyle = "#11ff00";
    ctx.fill();
    ctx.closePath();
  }

  drawRect(ctx, x, y, width, height, color='#410765') {
    ctx.beginPath();
    ctx.rect(x,y,width,height);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
  }

  drawCloud(ctx, x, y) {
    ctx.drawImage(this.cloudImg, x, y);
  }
}

export default Draw;
