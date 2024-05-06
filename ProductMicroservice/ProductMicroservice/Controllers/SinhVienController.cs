using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ProductMicroservice.Data;

namespace ProductMicroservice.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SinhVienController : ControllerBase
    {
        private readonly MyDbContext _dbContext;
        public SinhVienController(MyDbContext myDbContext)
        {
            _dbContext = myDbContext;
        }
    }
}
