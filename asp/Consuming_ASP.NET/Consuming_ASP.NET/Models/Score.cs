using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Consuming_ASP.NET.Models
{
    public class Score
    {
        public int Id { get; set; }
        public int Position { get; set; }
        public string UserId { get; set; }
        public int Points { get; set; }
    }
}