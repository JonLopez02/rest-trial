using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Consuming_ASP.NET.Models
{
    public class Car
    {

        private string brand;
        private string model;
        private float maxSpeedKmH;

        public Car()
        {
        }

        public string Brand { get; set; }
        public string Model { get; set; }
        public float MaxSpeedKmH { get; set; }

    }
}