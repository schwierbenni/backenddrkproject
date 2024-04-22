using Data;
using Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
namespace backend.Controllers;


[ApiController]
[Route("/api/[controller]")]
public class ProtocolController : ControllerBase
{
  private readonly ProtocolContext _context;

  public ProtocolController(ProtocolContext context)
  {
    _context = context;
  }

  // GET: api/protocol
  [HttpGet]
  public async Task<ActionResult<IEnumerable<Protocol>>> GetProtocol()
  {
    return await _context.Protocols.ToListAsync();
  }

  // GET: api/protocol/{id}
  [HttpGet("{id}")]
  public async Task<ActionResult<Protocol>> GetProtocol(long id)
  {
    var protocol = await _context.Protocols.FindAsync(id);

    if (protocol == null)
    {
      return NotFound();
    }

    return protocol;
  }

  // POST: api/protocol
  [HttpPost]
  public async Task<ActionResult<Protocol>> PostProtocol(Protocol protocol)
  {
    _context.Protocols.Add(protocol);
    await _context.SaveChangesAsync();

    return CreatedAtAction(nameof(GetProtocol), new { id = protocol.Id }, protocol);
  }

  // PUT: api/protocol/{id}
  [HttpPut("{id}")]
  public async Task<IActionResult> PutProtocol(long id, Protocol protocol)
  {
    if (id != protocol.Id)
    {
      return BadRequest();
    }

    _context.Entry(protocol).State = EntityState.Modified;

    try
    {
      await _context.SaveChangesAsync();
    }
    catch (DbUpdateConcurrencyException)
    {
      if (!ProtocolExists(id))
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

  // DELETE: api/protocol/{id}
  [HttpDelete("{id}")]
  public async Task<IActionResult> DeleteProtocol(long id)
  {
    var protocol = await _context.Protocols.FindAsync(id);
    if (protocol == null)
    {
      return NotFound();
    }

    _context.Protocols.Remove(protocol);
    await _context.SaveChangesAsync();

    return NoContent();
  }

  private bool ProtocolExists(long id)
  {
    return _context.Protocols.Any(e => e.Id == id);
  }
}
