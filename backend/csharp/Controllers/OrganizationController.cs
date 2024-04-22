using Data;
using Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
namespace backend.Controllers;


[ApiController]
[Route("/api/[controller]")]
public class OrganizationController : ControllerBase
{
  private readonly ProtocolContext _context;

  public OrganizationController(ProtocolContext context)
  {
    _context = context;
  }

  // GET: api/organization
  [HttpGet]
  public async Task<ActionResult<IEnumerable<Organization>>> GetOrganizations()
  {
    return await _context.Organizations.ToListAsync();
  }

  // GET: api/organization/{id}
  [HttpGet("{id}")]
  public async Task<ActionResult<Organization>> GetOrganization(long id)
  {
    var organization = await _context.Organizations.FindAsync(id);

    if (organization == null)
    {
      return NotFound();
    }

    return organization;
  }

  // POST: api/organization
  [HttpPost]
  public async Task<ActionResult<Organization>> PostOrganization(Organization organization)
  {
    _context.Organizations.Add(organization);
    await _context.SaveChangesAsync();

    return CreatedAtAction(nameof(GetOrganization), new { id = organization.Id }, organization);
  }

  // PUT: api/organization/{id}
  [HttpPut("{id}")]
  public async Task<IActionResult> PutOrganization(long id, Organization organization)
  {
    if (id != organization.Id)
    {
      return BadRequest();
    }

    _context.Entry(organization).State = EntityState.Modified;

    try
    {
      await _context.SaveChangesAsync();
    }
    catch (DbUpdateConcurrencyException)
    {
      if (!OrganizationExists(id))
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

  // DELETE: api/organization/{id}
  [HttpDelete("{id}")]
  public async Task<IActionResult> DeleteOrganization(long id)
  {
    var organization = await _context.Organizations.FindAsync(id);
    if (organization == null)
    {
      return NotFound();
    }

    _context.Organizations.Remove(organization);
    await _context.SaveChangesAsync();

    return NoContent();
  }

  private bool OrganizationExists(long id)
  {
    return _context.Organizations.Any(e => e.Id == id);
  }
}
