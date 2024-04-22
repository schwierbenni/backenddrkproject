using Data;
using Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
namespace backend.Controllers;


[ApiController]
[Route("/api/[controller]")]
public class UserController : ControllerBase
{
  private readonly ProtocolContext _context;

  public UserController(ProtocolContext context)
  {
    _context = context;
  }

  // GET: api/user
  [HttpGet]
  public async Task<ActionResult<IEnumerable<User>>> GetUser()
  {
    return await _context.Users.ToListAsync();
  }

  // GET: api/user/{id}
  [HttpGet("{id}")]
  public async Task<ActionResult<User>> GetUser(long id)
  {
    var user = await _context.Users.FindAsync(id);

    if (user == null)
    {
      return NotFound();
    }

    return user;
  }

  // POST: api/user
  [HttpPost]
  public async Task<ActionResult<User>> PostUser(User user)
  {
    _context.Users.Add(user);
    await _context.SaveChangesAsync();

    return CreatedAtAction(nameof(GetUser), new { id = user.Id }, user);
  }

  // PUT: api/user/{id}
  [HttpPut("{id}")]
  public async Task<IActionResult> PutUser(long id, User user)
  {
    if (id != user.Id)
    {
      return BadRequest();
    }

    _context.Entry(user).State = EntityState.Modified;

    try
    {
      await _context.SaveChangesAsync();
    }
    catch (DbUpdateConcurrencyException)
    {
      if (!UserExists(id))
      {
        return NotFound();
      }
      else
      {
        throw;
      }
    }

    return NoContent();
  }

  // DELETE: api/user/{id}
  [HttpDelete("{id}")]
  public async Task<IActionResult> DeleteUser(long id)
  {
    var user = await _context.Users.FindAsync(id);
    if (user == null)
    {
      return NotFound();
    }

    _context.Users.Remove(user);
    await _context.SaveChangesAsync();

    return NoContent();
  }

  private bool UserExists(long id)
  {
    return _context.Users.Any(e => e.Id == id);
  }
}
