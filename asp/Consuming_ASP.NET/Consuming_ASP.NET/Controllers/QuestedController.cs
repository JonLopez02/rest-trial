using Consuming_ASP.NET.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web;
using System.Web.Mvc;

namespace Consuming_ASP.NET.Controllers
{
    public class QuestedController : Controller
    {
        private string ip = "http://192.168.72.2:8080";
        // GET: Quested
        public async System.Threading.Tasks.Task<ActionResult> Index()
        {
            List<Quested> quested = new List<Quested>();
            using (var client = new HttpClient())
            {
                //Passing service base url  
                client.BaseAddress = new Uri(ip);   

                client.DefaultRequestHeaders.Clear();
                //Define request data format  
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

                //Sending request to find web api REST service resource GetAllEmployees using HttpClient  
                HttpResponseMessage Res = await client.GetAsync("?age=25");

                //Checking the response is successful or not which is sent using HttpClient  
                if (Res.IsSuccessStatusCode)
                {
                    //Storing the response details recieved from web api   
                    var EmpResponse = Res.Content.ReadAsStringAsync().Result;

                    //Deserializing the response recieved from web api and storing into the Employee list  
                    quested = JsonConvert.DeserializeObject<List<Quested>>(EmpResponse);

                }
                return View(quested);
            }
        }

        // GET: Quested/Details/5
        public ActionResult Details(int id)
        {
            return View();
        }

        // GET: Quested/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Quested/Create
        [HttpPost]
        public ActionResult Create(FormCollection collection)
        {
            try
            {
                // TODO: Add insert logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: Quested/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: Quested/Edit/5
        [HttpPost]
        public ActionResult Edit(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add update logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: Quested/Delete/5
        public ActionResult Delete(int id)
        {
            return View();
        }

        // POST: Quested/Delete/5
        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add delete logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }
    }
}
