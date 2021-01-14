using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Consuming_ASP.NET.Models
{
    public class Quested
    {
        private string category;
        private string type;
        private string difficulty;
        private string question;
        private string correctAnswer;
        private string[] incorrectAnswer;

        public Quested(string category, string type, string difficulty, string question, string correctAnswer, string[] incorrectAnswer)
        {
            this.category = category;
            this.type = type;
            this.difficulty = difficulty;
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.incorrectAnswer = incorrectAnswer;
        }

        public string Category { get; set; }
        public string Type { get; set; }
        public string Difficulty { get; set; }
        public string Question { get; set; }
        public string CorrectAnswer { get; set; }
        public string[] IncorrectAnswer { get; set; }

    }
}